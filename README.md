# ANTV - Assemblée Nationale TV

Application multiplateforme pour suivre les livestreams et replays de l'Assemblée Nationale française.

## 🎯 Plateformes Supportées

| Plateforme | Status | Notes |
|------------|--------|-------|
| 🤖 **Android** | ✅ Fonctionnel | Android 8.0+ (API 26+) |
| 🖥️ **Desktop** | ⚠️ À tester | macOS, Windows, Linux |
| 🍎 **iOS** | ⏳ En attente | Bloqué par incompatibilité Gradle 9 |

## 📋 Prérequis

### Pour le Développement
- **JDK 21** (fourni avec Android Studio)
- **Android Studio** Hedgehog ou plus récent
- **Xcode** 15+ (pour iOS, quand disponible)

### Versions Utilisées
- **Kotlin** : 2.1.0
- **Gradle** : 9.0-milestone-1
- **Compose Multiplatform** : 1.7.1
- **Android Gradle Plugin** : 8.7.3

## 🚀 Build & Run

### Android

#### Via Android Studio
1. Ouvrir le projet dans Android Studio
2. Sélectionner la configuration `androidApp`
3. Lancer l'application

#### Via Ligne de Commande
```bash
export JAVA_HOME="/Applications/Developpement/Android Studio.app/Contents/jbr/Contents/Home"
./gradlew :androidApp:assembleDebug

# APK généré dans :
# androidApp/build/outputs/apk/debug/androidApp-debug.apk
```

### Desktop
```bash
./gradlew desktopApp:run
```

### iOS
**⚠️ Actuellement non fonctionnel**

Voir [iOS_MIGRATION_STATUS.md](./iOS_MIGRATION_STATUS.md) pour plus de détails.

## 🏗️ Architecture

```
ANTV/
├── androidApp/          # Application Android
├── desktopApp/          # Application Desktop (Compose)
├── iosApp/              # Application iOS (Swift + Kotlin)
├── shared/              # Code partagé Kotlin Multiplatform
│   ├── commonMain/      # Code commun à toutes les plateformes
│   ├── androidMain/     # Code spécifique Android
│   ├── iosMain/         # Code spécifique iOS
│   └── desktopMain/     # Code spécifique Desktop
└── gradle/              # Configuration Gradle
```

## 📦 Dépendances Principales

- **Kotlin Multiplatform** - Code partagé
- **Compose Multiplatform** - UI multiplateforme
- **Moko Resources** - Gestion des ressources multiplateforme
- **Ktor** - Client HTTP
- **AndroidX Media3** - Lecteur vidéo (Android)
- **kotlinx.serialization** - Sérialisation JSON/XML
- **kotlinx.datetime** - Gestion des dates

## 🔧 Configuration

### Configuration Java
Le projet utilise JDK 21. Si vous n'utilisez pas Android Studio, configurez `JAVA_HOME` :

```bash
# macOS avec Android Studio
export JAVA_HOME="/Applications/Developpement/Android Studio.app/Contents/jbr/Contents/Home"

# OU avec SDKMAN
sdk install java 21.0.1-tem
sdk use java 21.0.1-tem
```

### Ressources
Les ressources sont dans `shared/src/commonMain/moko-resources/`

Structure :
```
moko-resources/
├── base/
│   └── strings.xml      # Chaînes de caractères
└── images/              # Images de l'app
```

## 🐛 Problèmes Connus

### iOS Build Failure
- **Problème** : Le plugin Kotlin Multiplatform n'est pas compatible avec Gradle 9 pour la génération de frameworks iOS
- **Status** : En attente d'une mise à jour du plugin
- **Workaround** : Voir [iOS_MIGRATION_STATUS.md](./iOS_MIGRATION_STATUS.md)

### Warnings de Dépréciation
Le projet contient des warnings pour des APIs dépréciées. Ils sont non-bloquants mais devraient être corrigés :
- `LocalLifecycleOwner` (Compose)
- `ClickableText` (Compose)
- `systemUiVisibility` (Android)

## 📱 Fonctionnalités

- ✅ Consultation des lives de l'Assemblée Nationale
- ✅ Accès aux replays
- ✅ Recherche de vidéos
- ✅ Playlists
- ✅ Support Chromecast (Android)
- ⏳ Support Android Auto (roadmap)
- ⏳ Support Android TV (roadmap)

## 🤝 Contribution

Les contributions sont bienvenues ! Sujets prioritaires :
1. Migration iOS vers Swift Package Manager (quand compatible)
2. Correction des warnings de dépréciation
3. Tests unitaires
4. Support Android TV

## 📄 Licence

Ce projet est développé par François GOGNET.
Code source : https://github.com/francois76/ANTV

**Note** : Les contenus vidéo et images proviennent du site de l'Assemblée Nationale
et ne sont pas intégrés dans cette application. Voir [crédits de l'Assemblée Nationale](https://www.assemblee-nationale.fr/dyn/info-site).

## 🔗 Liens Utiles

- [Assemblée Nationale - Vidéos](https://videos.assemblee-nationale.fr)
- [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)

---

**Dernière mise à jour** : 3 janvier 2026
**Version** : 1.2.3
