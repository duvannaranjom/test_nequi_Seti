Write-Host "Construyendo la imagen Docker de Franquicias API..." -ForegroundColor Green
docker-compose build

Write-Host "Iniciando los contenedores..." -ForegroundColor Green
docker-compose up -d

Write-Host "Verificando el estado de los contenedores..." -ForegroundColor Green
docker-compose ps

Write-Host "Para ver los logs de la aplicación, ejecute: docker-compose logs -f franquicias-api" -ForegroundColor Yellow
Write-Host "Para detener los contenedores, ejecute: docker-compose down" -ForegroundColor Yellow