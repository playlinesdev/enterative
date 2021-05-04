import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';


async function bootstrap() {
  const app = await NestFactory.create(AppModule, {
    cors: {
      origin: 'http://192.168.0.204:5000'
    }
  });

  const config = new DocumentBuilder()
    .setTitle('Enterative Api')
    .setDescription('An Api for Enterative system webservice functionalities')
    .setVersion('0.0.1')
    .build();

  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup('api', app, document);


  await app.listen(3000);
}
bootstrap();
