#!/bin/bash

# Variables
AWS_REGION="us-east-1"
STACK_NAME="franquicias-stack"
DB_PASSWORD="$(openssl rand -base64 12)"

echo "====== Despliegue de Franquicias API en AWS ======"
echo "Región: $AWS_REGION"
echo "Nombre de Stack: $STACK_NAME"

# 1. Configurar AWS CLI (suponiendo que ya estás autenticado)
echo "Verificando configuración de AWS CLI..."
aws --version
if [ $? -ne 0 ]; then
    echo "AWS CLI no está instalado o configurado correctamente."
    echo "Instala AWS CLI y configúralo con 'aws configure'"
    exit 1
fi

# 2. Crear el stack de CloudFormation para ECR
echo "Creando repositorio ECR..."
aws cloudformation deploy \
    --template-file ecr-stack.yaml \
    --stack-name ${STACK_NAME}-ecr \
    --region ${AWS_REGION}

# 3. Obtener URI del repositorio ECR
ECR_REPOSITORY_URI=$(aws cloudformation describe-stacks \
    --stack-name ${STACK_NAME}-ecr \
    --region ${AWS_REGION} \
    --query "Stacks[0].Outputs[?OutputKey=='RepositoryURI'].OutputValue" \
    --output text)

echo "Repositorio ECR creado: ${ECR_REPOSITORY_URI}"

# 4. Autenticarse en ECR
echo "Autenticando en ECR..."
aws ecr get-login-password --region ${AWS_REGION} | docker login --username AWS --password-stdin ${ECR_REPOSITORY_URI%/*}

# 5. Construir imagen de Docker
echo "Construyendo imagen Docker..."
docker build -t franquicias-api:latest -f Dockerfile.aws .

# 6. Etiquetar imagen
echo "Etiquetando imagen..."
docker tag franquicias-api:latest ${ECR_REPOSITORY_URI}:latest

# 7. Subir imagen a ECR
echo "Subiendo imagen a ECR..."
docker push ${ECR_REPOSITORY_URI}:latest

# 8. Desplegar stack completo con CloudFormation
echo "Desplegando infraestructura con CloudFormation..."
aws cloudformation deploy \
    --template-file franquicias-stack.yaml \
    --stack-name ${STACK_NAME} \
    --region ${AWS_REGION} \
    --parameter-overrides \
        DocDBPassword=${DB_PASSWORD} \
        ContainerImage=${ECR_REPOSITORY_URI}:latest \
    --capabilities CAPABILITY_IAM

# 9. Obtener URL de la aplicación
APP_URL=$(aws cloudformation describe-stacks \
    --stack-name ${STACK_NAME} \
    --region ${AWS_REGION} \
    --query "Stacks[0].Outputs[?OutputKey=='ApplicationURL'].OutputValue" \
    --output text)

echo "=============================================="
echo "¡Despliegue completado con éxito!"
echo "URL de la aplicación: ${APP_URL}"
echo "Contraseña de la base de datos: ${DB_PASSWORD}"
echo "=============================================="
echo "Nota: La aplicación puede tardar unos minutos en estar disponible."
echo "Puedes monitorear el estado del servicio en la consola de AWS ECS."
echo "=============================================="