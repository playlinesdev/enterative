import { Injectable, Logger } from '@nestjs/common';
import { MailerService } from 'src/mailer/mailer.service';
import * as fs from 'fs'

@Injectable()
export class AffiliateService {
    emailTemplate: String

    constructor(private mailerService: MailerService) {
        let file = fs.readFileSync('src/resources/affiliate_email_template.html')
        this.emailTemplate = file.toString()
    }

    async registerAffiliate(data: any, file: Express.Multer.File) {
        Logger.log(data)
        let content = this.mountEmail(data)

        let info = await this.mailerService.getTransporter().sendMail({
            from: process.env.EMAIL_ENTERATIVE_OWNER, // sender address
            to: process.env.AFFILIATE_REGISTER_NOTIFICATION_EMAIL, // list of receivers
            subject: "Solicitação de cadastro de afiliado", // Subject line
            text: JSON.stringify(data) ?? '', // plain text body
            html: content,
            attachments: [
                {
                    filename: file.originalname,
                    content: file.buffer
                }
            ]
        });

        return `E-mail enviado para ${info.envelope?.to[0]}`
    }

    mountEmail(data: any) {
        let emailContent = this.emailTemplate
        emailContent = emailContent.replace('$razaoSocial', data.razaoSocial)
        emailContent = emailContent.replace('$fantasia', data.fantasia)
        emailContent = emailContent.replace('$tipoLoja', data.tipoLoja)
        emailContent = emailContent.replace('$cnpj', data.cnpj)
        emailContent = emailContent.replace('$inscricaoEstadual', data.inscricaoEstadual)
        emailContent = emailContent.replace('$inscricaoMunicipal', data.inscricaoMunicipal)
        emailContent = emailContent.replace('$cpf', data.cpf)
        emailContent = emailContent.replace('$nomeResponsavel', data.nomeResponsavel)
        emailContent = emailContent.replace('$emailResponsavel', data.emailResponsavel)
        emailContent = emailContent.replace('$link', data.link)
        emailContent = emailContent.replace('$ramoAtividade', data.ramoAtividade)

        emailContent = emailContent.replace('$enderecoRua', data.enderecoRua)
        emailContent = emailContent.replace('$enderecoBairro', data.enderecoBairro)
        emailContent = emailContent.replace('$enderecoCidade', data.enderecoCidade)
        emailContent = emailContent.replace('$enderecoEstado', data.enderecoEstado)
        emailContent = emailContent.replace('$enderecoCep', data.enderecoCep)
        emailContent = emailContent.replace('$enderecoPais', data.enderecoPais)
        return emailContent
    }
}
