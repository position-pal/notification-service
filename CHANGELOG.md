## [1.0.0](https://github.com/position-pal/notification-service/compare/0.1.1...1.0.0) (2024-11-26)

### ⚠ BREAKING CHANGES

* **entrypoint:** leverage env variables for configurations

### Features

* add firebase notifications publisher adapter ([6079310](https://github.com/position-pal/notification-service/commit/6079310b06243a957a8591441efa240ed3558a98))
* add minimal entrypoint ([5570225](https://github.com/position-pal/notification-service/commit/55702255943696bc5c3d840f631a4771dcfe7063))
* **application:** add basic implementation of notification publisher ([e9e6c79](https://github.com/position-pal/notification-service/commit/e9e6c79d7def7e53f742ff1906ccc2d1e0a130c5))
* **application:** add repository and services interfaces ([3cbd3be](https://github.com/position-pal/notification-service/commit/3cbd3beb64fc81ca3c871e855df45920181a94ee))
* **domain:** add UserToken interface ([dc02428](https://github.com/position-pal/notification-service/commit/dc024284ff9576f0eb8755e19019671084c8361c))
* **grpc:** add grpc token service adapter ([3700dc5](https://github.com/position-pal/notification-service/commit/3700dc5bf13e53aac8ed4f2affd9b5a5d1525c44))
* **mom:** add rabbitmq adapters ([2251034](https://github.com/position-pal/notification-service/commit/2251034a335b06b41f6e3c1545ccbd1649150f3a))
* **storage:** add postgres groups repository adapter ([65ec75b](https://github.com/position-pal/notification-service/commit/65ec75b7ab12927e82849863df5e269de09a6d6a))
* **storage:** add postgres user tokens repository adapter ([fa44383](https://github.com/position-pal/notification-service/commit/fa44383e20eb5665066b08b1c778d5f2900c1f59))

### Dependency updates

* **deps:** update dependency gradle to v8.11 ([#3](https://github.com/position-pal/notification-service/issues/3)) ([e1c4202](https://github.com/position-pal/notification-service/commit/e1c4202156e8378c59f3e511f72475bdcf0b9a13))
* **deps:** update dependency gradle to v8.11.1 ([#7](https://github.com/position-pal/notification-service/issues/7)) ([3854de4](https://github.com/position-pal/notification-service/commit/3854de40494c168b8e300dd82de50859d693e831))
* **deps:** update plugin com.gradle.develocity to v3.18.2 ([#4](https://github.com/position-pal/notification-service/issues/4)) ([9c560a0](https://github.com/position-pal/notification-service/commit/9c560a06ae8cf1063633ddf6eb6b5fbe506540b1))
* **deps:** update plugin kotlin-qa to v0.70.1 ([#5](https://github.com/position-pal/notification-service/issues/5)) ([7cb7b3b](https://github.com/position-pal/notification-service/commit/7cb7b3b3aec79bcb6d936ec27722cf1de42661d7))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v2.0.14 ([#6](https://github.com/position-pal/notification-service/issues/6)) ([7be8601](https://github.com/position-pal/notification-service/commit/7be8601e28cee8661e001b64d25f6a18dee3e4bd))
* **deps:** update plugin org.danilopianini.gradle-pre-commit-git-hooks to v2.0.15 ([#8](https://github.com/position-pal/notification-service/issues/8)) ([8357ac3](https://github.com/position-pal/notification-service/commit/8357ac360886b1b6bc396d01203ad4afa452199d))

### Bug Fixes

* **build:** inject env variables only after having evaluated subprojects ([948765b](https://github.com/position-pal/notification-service/commit/948765bc9425f960e9a3c0ea162344d6cfa9517a))
* **entrypoint:** leverage env variables for configurations ([44b3773](https://github.com/position-pal/notification-service/commit/44b37731e1f95d58045f5673a37404f0d9561d18))

### Documentation

* **application:** improve api documentation ([7757e19](https://github.com/position-pal/notification-service/commit/7757e19e98be17e70be6d5eda839091cc7bcdc84))
* **storage:** fix typo ([e7496df](https://github.com/position-pal/notification-service/commit/e7496dfb372d62ecd6cd1efc84db21faf7fb1bf1))

### Performance improvements

* **storage:** use hikari connection pool ([819acec](https://github.com/position-pal/notification-service/commit/819acec3ab3ec42fff1fdebd4ce6d6e98bb0431a))

### Build and continuous integration

* add dotenv and utils functions to buildSrc ([12136bd](https://github.com/position-pal/notification-service/commit/12136bd6d100c18dffaf3433dfcf2113867cc041))
* configure gradle to not run docker required test in ci on windows and macos runners ([7e0a9e2](https://github.com/position-pal/notification-service/commit/7e0a9e2dba66451b1d65754b8f315d559b3cc086))
* drop jvm 11 from matrix ([be38ee8](https://github.com/position-pal/notification-service/commit/be38ee8a6c386e75345d0aa229867147167b9676))
* update env configuration, add docker compose plugin and submodule definitions ([a0c9a8e](https://github.com/position-pal/notification-service/commit/a0c9a8e2af809c962618b2d082dd0ac0a7ce8fb4))
* use username and token for accessing shared kernel packages ([8a147aa](https://github.com/position-pal/notification-service/commit/8a147aac78695087c125322f89a46829ab8974aa))

### Refactoring

* **commons:** extract ConnectionFactory and result flatmap ([895f01c](https://github.com/position-pal/notification-service/commit/895f01c4c5111085b9192e70d3d9df93431cf01f))
* fix dependencies and style issues ([1403d15](https://github.com/position-pal/notification-service/commit/1403d15cf9c42e1731f2d91f641da4a80daf8da5))
* **messages:** enclose configuration in an object factory, improve coroutines dispatcher and dry logic ([bfed3bb](https://github.com/position-pal/notification-service/commit/bfed3bb93e2319d157b8e30d01aaef63e53f12ed))
* **presentation:** use qualified this ([71a48ba](https://github.com/position-pal/notification-service/commit/71a48ba2a472a9411dbc759b3cba211bd5dff48e))
* **storage:** wrap postgres configuration in an enclosing factory object ([5a894cf](https://github.com/position-pal/notification-service/commit/5a894cfa109fe19e91f8e41211a6ed4c1fcbec73))

## [0.1.1](https://github.com/position-pal/notification-service/compare/0.1.0...0.1.1) (2024-11-15)

### Dependency updates

* **deps:** update dependency semantic-release-preconfigured-conventional-commits to v1.1.115 ([#2](https://github.com/position-pal/notification-service/issues/2)) ([05fb743](https://github.com/position-pal/notification-service/commit/05fb7430d3309e1721937dc0ead6393871a3805c))

### Documentation

* update readme ([44f0c74](https://github.com/position-pal/notification-service/commit/44f0c74117f479df3eeed7fba88d5bb8d03cd6e6))

### General maintenance

* remove changelog ([4c3ccd2](https://github.com/position-pal/notification-service/commit/4c3ccd2c83345f7b1e9b8911c8715e5b35e226ff))
