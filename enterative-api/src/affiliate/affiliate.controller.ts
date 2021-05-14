import { Body, Controller, HttpException, HttpStatus, Post, UploadedFile, UseInterceptors } from '@nestjs/common';
import { FileInterceptor } from '@nestjs/platform-express';
import { ApiTags } from '@nestjs/swagger';
import { AffiliateService } from './affiliate.service';

@ApiTags('Affiliate')
@Controller('affiliate')
export class AffiliateController {

    constructor(private affiliateService: AffiliateService) { }

    @Post('register')
    @UseInterceptors(FileInterceptor('facadePicture'))
    async registerAffiliate(@UploadedFile() file: Express.Multer.File, @Body() data: any) {
        let requestBodyValidation = this.validateRequestBody(data)
        if (!!requestBodyValidation)
            throw new HttpException(requestBodyValidation, HttpStatus.BAD_REQUEST)
        return this.affiliateService.registerAffiliate(data, file)
    }

    validateRequestBody(data: any): String {
        if (!data) return 'Invalid empty request'
        if (!data.razaoSocial) return 'Campo obrigatório: Razão social'
        if (!data.fantasia) return 'Campo obrigatório: Fantasia'
        if (!data.tipoLoja) return 'Campo obrigatório: Tipo de Loja'
        if (!data.cnpj) return 'Campo obrigatório: Cnpj'
        // if (!data.inscricaoEstadual) return 'Campo obrigatório: Inscrição Estadual'
        // if (!data.inscricaoMunicipal) return 'Campo obrigatório: Inscrição Municipal'
        if (!data.cpf) return 'Campo obrigatório: Cpf'
        if (!data.nomeResponsavel) return 'Campo obrigatório: Nome do Responsável'
        if (!data.emailResponsavel) return 'Campo obrigatório: Email do Responsável'
        if (!data.link) return 'Campo obrigatório: Link'
        if (!data.ramoAtividade) return 'Campo obrigatório: Ramo de Atividade'
        if (!data.enderecoRua) return 'Campo obrigatório: Endereço - Rua'
        if (!data.enderecoBairro) return 'Campo obrigatório: Endereço - Bairro'
        if (!data.enderecoCidade) return 'Campo obrigatório: Endereço - Cidade'
        if (!data.enderecoEstado) return 'Campo obrigatório: Endereço - Estado'
        if (!data.enderecoCep) return 'Campo obrigatório: Endereço - Cep'
        if (!data.enderecoPais) return 'Campo obrigatório: Endereço - País'
    }
}
