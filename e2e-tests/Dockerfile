##################
# Build image
##################
FROM node AS build-img

# Defining build folder
WORKDIR /build

# Adding package.json and getting dependencies
COPY package.json .
COPY package-lock.json .
RUN npm ci

# Adding sources
COPY ./src ./src

##################
# Run image
##################
FROM node

# Dockerize is needed to sync containers startup
ENV DOCKERIZE_VERSION v0.6.0
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz

# Defining runtime folder
WORKDIR /srv/test

# Adding the app
COPY --from=build-img /build .

# Running the app
CMD npm run test
