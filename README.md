# BaseApp – Aplicativo Base para Projetos Android Modernos

O **BaseApp** é um aplicativo Android desenvolvido em **Kotlin**, projetado para servir como
**base sólida e moderna para criação de novos aplicativos**.  
Ele reúne as melhores práticas, arquitetura atualizada, bibliotecas modernas e integrações comuns
à maioria dos projetos reais permitindo iniciar novos apps com estrutura pronta e escalável.

A proposta do BaseApp é funcionar como um **template profissional**, incluindo:

- Arquitetura limpa (MVVM + camadas bem definidas)
- Jetpack Compose como UI padrão
- Navegação declarativa
- Room como persistência local
- Serviços integrados (Firebase Cloud Messaging, biometria)
- Comunicação com API REST baseada no projeto ApiRestfulSlimPHP4  
  🔗 https://github.com/fabioedusantos/ApiRestfulSlimPHP4
- Estrutura organizada para expansão e modularização

O objetivo é acelerar o desenvolvimento de novas aplicações, mantendo **padrão, organização e escalabilidade** desde o início.

---

## Tecnologias & Arquitetura

### Arquitetura
- **MVVM (Model–View–ViewModel)**
- Navegação declarativa com **Navigation Compose**
- **Repository Pattern**
- Camadas **data**, **domain**, **ui**
- **StateFlow** e estados reativos
- DI manual inspirado em Hilt

### UI
- **Jetpack Compose**
- **Material 3**
- **Coil Compose** para carregamento de imagens

### Persistência
- **Room Database**
- DAOs, Entities e Schemas exportados em `/app/schemas`

### Integrações
- **Firebase Cloud Messaging (FCM)**
- Suporte a **Biometria Android**

---

## Estrutura Completa do `src/` do Projeto

Abaixo está o detalhamento completo e organizado da estrutura de pastas do diretório `src/main/java`, conforme o projeto BaseApp:

```
src/main/java/br/com/fbsantos/baseapp/
│
├── data/
│   ├── local/
│   │   ├── dao/                # Interfaces DAO do Room
│   │   ├── database/           # Configuração do RoomDatabase
│   │   └── entities/           # Entidades (tabelas do banco)
│   │
│   ├── remote/
│   │   ├── api/                # Interfaces de comunicação com a API
│   │   └── models/             # Modelos recebidos/enviados ao servidor
│   │
│   ├── repository/
│   │   ├── impl/               # Implementações dos repositórios
│   │   └── interfaces/         # Contratos Repository
│   │
│   └── mappers/                # Conversores DTO ↔ domain models
│
├── domain/
│   ├── model/                  # Modelos de regra de negócio (core)
│   └── usecase/                # Casos de uso (se aplicável)
│
├── ui/
│   ├── components/             # Componentes reutilizáveis Compose
│   ├── navigation/             # Graph, NavHost e rotas
│   ├── screens/                # Telas da aplicação
│   │   ├── login/
│   │   ├── home/
│   │   ├── splash/
│   │   └── settings/
│   ├── theme/                  # Cores, tipografia, shapes e temas
│   └── viewmodel/              # ViewModels da aplicação
│
├── services/
│   ├── firebase/               # FirebaseMessagingService
│   └── biometric/              # Implementação de autenticação biométrica
│
└── utils/
    ├── extensions/             # Extensões Kotlin úteis
    ├── constants/              # Constantes globais
    └── helpers/                # Classes auxiliares e formatadores
```

---

## Execução

### Clonar o repositório
```
git clone https://github.com/usuario/BaseApp.git
cd BaseApp
```

### Adicionar `google-services.json`
Colocar em:
```
app/google-services.json
```

### Build
```
./gradlew assembleDebug
```

---

## API

O consumo de dados é baseado no backend:

**ApiRestfulSlimPHP4**  
https://github.com/fabioedusantos/ApiRestfulSlimPHP4

A comunicação segue boas práticas:
- Retrofit (quando utilizado)
- Repository Pattern
- Tratamento de erros
- Suporte a autenticação (customizável)

---

## Sobre o Autor
Este projeto foi desenvolvido por Fábio Eduardo Santos. Conecte-se:
* **Email:** fabioedusantos@gmail.com
* **Website:** [fbsantos.com.br](https://fbsantos.com.br)
* **LinkedIn:** [Fábio Eduardo Santos](https://www.linkedin.com/in/fabioedusantos/)
