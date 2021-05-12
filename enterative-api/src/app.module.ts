import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { ServeStaticModule } from '@nestjs/serve-static';
import { join } from 'path';
import { AffiliateModule } from './affiliate/affiliate.module';
import { ConfigModule } from '@nestjs/config';

@Module({
  imports: [
    ConfigModule.forRoot(),
    ServeStaticModule.forRoot({
      rootPath: join(__dirname, '..', 'web'), renderPath: '/web'
    }),
    AffiliateModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule { }
