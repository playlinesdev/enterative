import { Injectable } from '@nestjs/common';
import * as nodemailer from 'nodemailer'

@Injectable()
export class MailerService {
    emailSettings: { host: string; port: number; auth: { user: string; pass: string; }; };

    constructor() {
        this.emailSettings = {
            host: process.env.SMTP_MAIL_HOST,
            port: Number(process.env.SMTP_MAIL_PORT ?? '0'),
            auth: {
                user: process.env.SMTP_MAIL_USERNAME,
                pass: process.env.SMTP_MAIL_PASSWORD
            },
        }
    }

    getTransporter() {
        return nodemailer.createTransport({
            host: this.emailSettings.host,
            port: this.emailSettings.port,
            secure: false, // true for 465, false for other ports
            auth: this.emailSettings.auth
        });
    }
}
