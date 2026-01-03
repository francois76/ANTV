# État de la Migration iOS vers SPM

## ⚠️ Problème Actuel

La migration vers Swift Package Manager (SPM) est **bloquée** par une incompatibilité entre :
- **Gradle 9.0-milestone-1**
- **Kotlin Multiplatform 2.1.0**
- API `binaries.framework` du plugin Kotlin

### Erreur Rencontrée
```
org/gradle/api/internal/plugins/DefaultArtifactPublicationSet
```

Cette erreur indique que le plugin Kotlin Multiplatform accède à des APIs internes de Gradle qui ont changé dans Gradle 9.

## 📊 État Actuel

### ✅ Ce qui Fonctionne
- **Android** : Compile parfaitement avec Gradle 9 + Kotlin 2.1.0
- **Desktop** : Devrait compiler (non testé)
- **iOS Kotlin code** : Compile en bibliothèques Kotlin (klib)

### ❌ Ce qui Ne Fonctionne Pas
- **iOS Framework generation** : Impossible de générer le framework iOS/XCFramework
- **CocoaPods integration** : Plugin non compatible avec Gradle 9
- **L'app iOS** : Ne peut pas être buildée sans le framework

## 🔧 Solutions Possibles

### Solution 1 : Attendre une Mise à Jour (Recommandé pour Production)
**Avantages :**
- Reste sur les versions modernes (Gradle 9, Kotlin 2.1)
- Bénéficie des dernières fonctionnalités et corrections

**Actions :**
- Surveiller les releases de Kotlin : https://github.com/JetBrains/kotlin/releases
- Chercher un fix pour Gradle 9 dans les versions 2.1.x ou 2.2.0

**Temps estimé :** Quelques semaines à quelques mois

### Solution 2 : Downgrader Gradle pour iOS (Approche Hybride)
Garder Gradle 9 pour le développement Android, mais utiliser Gradle 8.5 pour les builds iOS.

**Configuration :**
1. Créer un `gradle-ios.properties` avec Gradle 8.5
2. Créer des scripts séparés pour iOS
3. Utiliser Gradle 8.5 uniquement pour `assembleXCFramework`

**Avantages :**
- Android reste moderne
- iOS fonctionne

**Inconvénients :**
- Configuration plus complexe
- Deux versions de Gradle à maintenir

### Solution 3 : Downgrader Complètement (Solution Temporaire Rapide)
Revenir à Gradle 8.5 + Kotlin 1.9.x pour tout le projet.

**Comment faire :**
```bash
# Dans gradle/wrapper/gradle-wrapper.properties
distributionUrl=https\://services.gradle.org/distributions/gradle-8.5-bin.zip

# Dans gradle/libs.versions.toml
kotlin = "1.9.20"
android-gradle-plugin = "8.1.2"
compose-multiplatform = "1.5.11"
```

**Avantages :**
- iOS fonctionne immédiatement
- Configuration plus simple

**Inconvénients :**
- Perd les bénéfices de Kotlin 2.1 et Gradle 9
- Devra re-migrer plus tard

### Solution 4 : Build Manuel des Frameworks (Workaround Avancé)
Compiler manuellement les frameworks iOS avec `cinterop` et scripts custom.

**Complexité :** Élevée
**Recommandé :** Non, sauf besoin urgent

## 🎯 Recommandation

Pour un **projet en développement actif** :
→ **Solution 2 (Hybride)** ou **Solution 3 (Downgrade temporaire)**

Pour un **projet en production** :
→ **Solution 1 (Attendre)** et continuer sur Android en attendant

## 📝 Configuration Préparée (Commentée)

Le code pour SPM/XCFramework est **déjà écrit** dans `shared/build.gradle.kts` mais **commenté**.

Quand le plugin Kotlin sera compatible, il suffira de :
1. Décommenter le bloc `XCFramework` (lignes 26-41)
2. Lancer `./gradlew :shared:assembleSharedDebugXCFramework`
3. Intégrer le XCFramework dans Xcode via SPM

## 🔗 Ressources

- [Kotlin Multiplatform + Gradle 9 Issue](https://youtrack.jetbrains.com/issues/KT)
- [Kotlin Releases](https://github.com/JetBrains/kotlin/releases)
- [KMP iOS Documentation](https://kotlinlang.org/docs/multiplatform-ios.html)

---

**Dernière mise à jour :** 3 janvier 2026
**Status :** En attente de compatibilité Kotlin plugin avec Gradle 9
