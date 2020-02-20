### STAGE 1: Build ###
FROM node:12.14-alpine AS build
WORKDIR /usr/src/app
COPY package.json ./
RUN npm install
RUN npm install -g @angular/cli@8.3.23
COPY . .
RUN ng build --prod
### STAGE 2: Run ###
FROM nginx:1.17.1-alpine
COPY --from=build /usr/src/app/dist/kyobee /usr/share/nginx/html


# expose port 80
#EXPOSE 80

# run nginx
#CMD ["nginx", "-g", "daemon off;"]