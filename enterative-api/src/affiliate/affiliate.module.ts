import { Module } from '@nestjs/common';
import { MailerService } from 'src/mailer/mailer.service';
import { AffiliateController } from './affiliate.controller';
import { AffiliateService } from './affiliate.service';

@Module({
  controllers: [AffiliateController],
  providers: [MailerService, AffiliateService]
})
export class AffiliateModule { }
