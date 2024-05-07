

`
docker pull mongo
`



`
docker run -d -p 27017:27017 --name mongodb1 -e MONGO_INITDB_ROOT_USERNAME=test -e MONGO_INITDB_ROOT_PASSWORD=123456789 -v $PWD/db:/data/db mongo
`
