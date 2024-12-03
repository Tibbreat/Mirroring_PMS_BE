# Build the Docker image
echo "Building Docker image $REPO_NAME:$TAG ..."
docker build -t tungb12ok/pms .


echo "Pushing Docker image $REPO_NAME:$TAG to Docker Hub..."
docker push tungb12ok/pms
echo Done!
pause