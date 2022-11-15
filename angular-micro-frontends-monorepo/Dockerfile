### STAGE 1: Build ###
FROM node:16-alpine AS build
ARG APP
ENV APP=${APP}
WORKDIR /usr/src/app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build:${APP}

### STAGE 2: Run ###
FROM nginx:1.17.1-alpine
ARG APP
ENV APP=${APP}
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /usr/src/app/dist/${APP} /usr/share/nginx/html

EXPOSE 4201
EXPOSE 4202
