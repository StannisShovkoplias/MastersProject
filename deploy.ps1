param (
    [string]$idFilePath = "current_id.txt"
)

if (Test-Path $idFilePath) {
    $currentId = Get-Content $idFilePath
} else {
    $currentId = 0
}

$currentId = [int]$currentId

$currentId++

$currentId | Set-Content $idFilePath

$ErrorActionPreference = "Stop"

./gradlew clean
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

./gradlew assemble

docker build -t acheron1232/prod:$currentId.0 .
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

docker push acheron1232/prod:$currentId.0
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
