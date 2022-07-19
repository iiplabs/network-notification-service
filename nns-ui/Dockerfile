FROM node:14.20.0-alpine

WORKDIR /app
COPY . ./

RUN apk update && apk upgrade
RUN apk add git

RUN npm install
RUN npm run build

# expose 3000 on container
EXPOSE 3000

# set app serving to permissive / assigned
ENV NUXT_HOST=0.0.0.0
# set app port
ENV NUXT_PORT=3000

# start the app
CMD [ "nuxt", "start" ]
