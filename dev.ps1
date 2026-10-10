# DayLoom dev: levanta backend (mongo + MS disponibles) y frontend.
# Uso:  .\dev.ps1            (usa imagenes ya construidas)
#       .\dev.ps1 -Build     (reconstruye imagenes backend)
param([switch]$Build)

$ErrorActionPreference = "Stop"
$ROOT = Split-Path -Parent $MyInvocation.MyCommand.Path
$BACKEND = Join-Path $ROOT "backend"
$FRONTEND = Join-Path $ROOT "frontend"
$WEB_URL = "http://localhost:5173"

function Fail($msg) { Write-Host "ERROR: $msg" -ForegroundColor Red; exit 1 }

function EnvVal($file, $name, $default) {
  $line = Get-Content $file -ErrorAction SilentlyContinue | Where-Object { $_ -match "^$name=" } | Select-Object -First 1
  if ($line) { return ($line -replace "^$name=", "").Trim() }
  return $default
}

# 1. Docker
docker ps 2>$null | Out-Null
if ($LASTEXITCODE -ne 0) { Fail "Docker Desktop apagado. Prendelo e intenta de nuevo." }

# 2. backend/.env (gitignored, nunca se commitea)
if (-not (Test-Path (Join-Path $BACKEND ".env"))) {
  Fail "falta backend/.env (copialo desde backend/.env.example y completa MONGODB_URI/JWT_SECRET)"
}

# 3. Backend: mongo + MS + gateway (solo el gateway publica puerto)
$BIND = EnvVal (Join-Path $BACKEND ".env") "TAILNET_BIND_IP" "127.0.0.1"
$GPORT = EnvVal (Join-Path $BACKEND ".env") "GATEWAY_PORT" "8080"
$GW_URL = "http://${BIND}:${GPORT}"
$API_URL = $GW_URL
Push-Location $BACKEND
try {
  if ($Build) { docker compose up -d --build mongo ms-identity-admin ms-habits ms-planner ms-gateway }
  else { docker compose up -d mongo ms-identity-admin ms-habits ms-planner ms-gateway }
  if ($LASTEXITCODE -ne 0) { Fail "docker compose fallo (revisa puertos o TAILNET_BIND_IP)" }
} finally { Pop-Location }

$ok = $false
for ($i = 0; $i -lt 30; $i++) {
  try {
    $h = Invoke-RestMethod -Uri "$GW_URL/actuator/health" -TimeoutSec 3
    if ($h.status -eq "UP") { $ok = $true; break }
  } catch {}
  Start-Sleep -Seconds 3
}
if (-not $ok) { Fail "backend no partio (revisa: docker logs dayloom-gateway)" }
Write-Host "backend OK en $GW_URL" -ForegroundColor Green

# 4. Frontend
if (-not (Test-Path (Join-Path $FRONTEND "node_modules"))) {
  Write-Host "instalando dependencias frontend..."
  Push-Location $FRONTEND
  try { npm install --no-audit --no-fund } finally { Pop-Location }
}
try { Start-Process $WEB_URL } catch {}
$env:VITE_API_URL = $API_URL
Push-Location $FRONTEND
try {
  Write-Host "frontend en $WEB_URL (Ctrl+C para salir; backend queda arriba, para bajarlo: docker compose stop en backend/)"
  npm run dev
} finally { Pop-Location }
