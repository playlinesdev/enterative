import { Body, Controller, Get, Logger, Param, Post, UploadedFile, UseInterceptors } from '@nestjs/common';
import { FileInterceptor } from '@nestjs/platform-express';
import { ApiTags } from '@nestjs/swagger';
import * as nodemailer from 'nodemailer'

@ApiTags('Affiliate')
@Controller('affiliate')
export class AffiliateController {
    emailSettings: { host: string; port: number; auth: { user: string; pass: string; }; secure: string; };

    constructor() {
        this.emailSettings = {
            host: 'smtp.office365.com',
            port: 587,
            auth: {
                user: 'rafante2@gmail.com',
                pass: 'Mel05072103'
            },
            secure: 'STARTTLS'
        }
    }

    @Post('register')
    async registerAffiliate(@Body() data) {
        Logger.log(data)
        let transporter = nodemailer.createTransport({
            host: this.emailSettings.host,
            port: this.emailSettings.port,
            secure: false, // true for 465, false for other ports
            auth: this.emailSettings.auth
        });

        let info = await transporter.sendMail({
            from: '"Fred Foo 👻" <rafante2@hotmail.com>', // sender address
            to: "rafanteapp@hotmail.com", // list of receivers
            subject: "Hello ✔", // Subject line
            text: "Hello world?", // plain text body
            html: "<b>Hello world?</b>", // html body
        });
    }

    @Post('upload')
    @UseInterceptors(FileInterceptor('file'))
    uploadFile(@UploadedFile() file: Express.Multer.File) {
        console.log(file);
    }
}
