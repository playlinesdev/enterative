import { Logger } from '@nestjs/common';
import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';
import { ExpressAdapter } from '@nestjs/platform-express';
import * as express from 'express'
import * as fs from 'fs'
import * as http from 'http'
import * as https from 'https'

const httpsOptions = {
  key: fs.readFileSync('src/secrets/key.pem'),
  cert: fs.readFileSync('src/secrets/cert.pem')
}

const server = express();

async function bootstrap() {
  const app = await NestFactory.create(
    AppModule,
    new ExpressAdapter(server)
  );

  const config = new DocumentBuilder()
    .setTitle('Enterative Api')
    .setDescription('An Api for Enterative system webservice functionalities')
    .setVersion('0.0.1')
    .build();

  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup('api', app, document);

  app.enableCors({
    origin: true,
    methods: 'GET,HEAD,PUT,PATCH,POST,DELETE,OPTIONS',
    credentials: true,
  })
  await app.init();

  let httpPort = process.env.HTTP_PORT ?? 8080
  http.createServer(server).listen(httpPort);
  Logger.log(`Api listening http on port ${httpPort}`)

  let httpsPort = process.env.HTTPS_PORT ?? 3443
  Logger.log(`Api listening https on port ${httpsPort}`)

  try {
    let pfxHttpsCertFile = fs.readFileSync(process.env.SSL_CERTIFICATE_PATH)

    if (pfxHttpsCertFile)
      https.createServer({
        pfx: fs.readFileSync(process.env.SSL_CERTIFICATE_PATH),
        passphrase: process.env.SSL_CERTIFICATE_PASSWORD ?? ''
      }, server).listen(httpsPort);
  } catch (error) {
    Logger.error('Https not enabled because certificate file is not present')
  }
}
bootstrap();
