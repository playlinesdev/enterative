import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { ServeStaticModule } from '@nestjs/serve-static';
import { join } from 'path';
import { AffiliateModule } from './affiliate/affiliate.module';

@Module({
  imports: [
    ServeStaticModule.forRoot({
      rootPath: join(__dirname, '..', 'client/web'),
    }),
    AffiliateModule,
  ],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule { }
