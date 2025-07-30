# VillaMaster AI

VillaMaster AI est une application Android native de gestion de propriétés (PMS) avec intelligence artificielle intégrée. L'application est conçue pour fonctionner entièrement hors ligne avec une base de données locale Room.

## 🚀 Fonctionnalités Principales

### 🤖 Assistant IA Intégré
- Interface de chat conversationnel avec Gemini AI
- Gestion des données par commandes vocales
- Support des images avec analyse IA
- Actions automatisées sur la base de données

### 📊 Tableau de Bord Intelligent
- KPIs en temps réel (taux d'occupation, revenus, etc.)
- Arrivées et départs du jour
- Statistiques visuelles avec Material Design 3

### 📋 Gestion Complète des Réservations
- Création, modification et suivi des réservations
- Statuts visuels avec pastilles colorées
- Calculs automatiques des montants
- Vérification de disponibilité en temps réel

### 📅 Système de Disponibilités
- Vérification rapide des créneaux libres
- Interface calendrier intuitive
- Alertes de conflits automatiques

### 👥 Gestion des Clients
- Base de données clients complète
- Système de clients VIP
- Historique des séjours

### 🏡 Catalogue des Villas
- Fiches détaillées des propriétés
- Galerie photos et équipements
- Tarification flexible

## 🔧 Architecture Technique

### Technologies Utilisées
- **Langage**: Kotlin 100%
- **UI**: Jetpack Compose + Material Design 3
- **Architecture**: MVVM (Model-View-ViewModel)
- **Base de Données**: Room Database (SQLite)
- **IA**: Google AI SDK (Gemini)
- **Injection de Dépendances**: Hilt
- **Asynchronisme**: Coroutines Kotlin

### Structure du Projet
```
app/
├── src/main/java/com/villamaster/ai/
│   ├── data/
│   │   ├── database/          # Room Database, DAOs, Entities
│   │   └── repository/        # Repositories pour l'accès aux données
│   ├── ui/
│   │   ├── screen/           # Écrans Compose
│   │   ├── component/        # Composants réutilisables
│   │   ├── navigation/       # Navigation
│   │   └── theme/           # Thème Material Design 3
│   ├── service/             # Services (IA, Backup, Telegram)
│   ├── worker/              # Background Workers
│   └── di/                  # Modules d'injection Hilt
```

## 💾 Gestion des Données

### Sauvegarde Automatique
- Sauvegarde quotidienne automatique dans le stockage privé
- Rotation des sauvegardes (7 dernières conservées)
- Processus silencieux via WorkManager

### Import/Export Manuel
- **Export**: Sauvegarde complète au format JSON dans Téléchargements
- **Import**: Restauration depuis fichier de sauvegarde
- **Reset**: Réinitialisation avec données de démonstration

### Sécurité des Données
- Stockage local sécurisé avec Room
- Chiffrement des données sensibles
- Confirmations multiples pour actions destructives

## 🎨 Interface Utilisateur

### Navigation
- **Bottom Navigation**: 4 onglets principaux (Assistant, Dashboard, Réservations, Disponibilités)
- **Menu Plus**: Accès aux écrans secondaires (Clients, Villas, Paramètres)
- **Navigation fluide** avec animations Material

### Design System
- **Material Design 3** avec couleurs dynamiques
- **Thème adaptatif** (clair/sombre)
- **Composants cohérents** et accessibles
- **Animations** et micro-interactions

## 🔌 Intégrations Externes

### Bot Telegram
- Service en arrière-plan (ForegroundService)
- Notifications push des réservations
- Commandes de gestion à distance

### Génération PDF
- Factures et confirmations automatiques
- Partage natif Android (Share Sheet)
- Templates personnalisables

## 📱 Compatibilité

- **Android minimum**: API 24 (Android 7.0)
- **Android cible**: API 34 (Android 14)
- **Architecture**: ARM64, ARM32, x86_64
- **Taille**: ~15 MB (APK optimisé)

## 🛠️ Installation et Configuration

### Prérequis
- Android Studio Hedgehog ou plus récent
- JDK 8 ou plus récent
- SDK Android 34

### Configuration
1. Cloner le projet
2. Ouvrir dans Android Studio
3. Configurer la clé API Gemini dans `AIService.kt`
4. Synchroniser les dépendances Gradle
5. Compiler et installer sur appareil/émulateur

### Variables d'Environnement
```kotlin
// Dans AIService.kt
private val generativeModel = GenerativeModel(
    modelName = "gemini-pro",
    apiKey = "VOTRE_CLE_API_GEMINI"
)
```

## 📄 Permissions Requises

- `INTERNET`: Communication avec l'API Gemini
- `READ_EXTERNAL_STORAGE`: Lecture des fichiers de sauvegarde
- `WRITE_EXTERNAL_STORAGE`: Export des données
- `READ_MEDIA_IMAGES`: Accès aux images (Android 13+)
- `FOREGROUND_SERVICE`: Service Telegram en arrière-plan

## 👨‍💻 Développeur

**Yanis Alimamar**
- Expert Android/Kotlin
- Spécialiste Jetpack Compose
- Architecture MVVM/Clean Architecture

## 📝 Licence

Projet propriétaire - Tous droits réservés

---

*VillaMaster AI - La gestion de propriétés réinventée avec l'intelligence artificielle*