# 🧠 MindSpace

**MindSpace** is a Kotlin Android app that helps you **track your emotions, reflect on your mental well-being, and visualize your feelings** through AI-generated mood boards.  
Built for **Shipaton 2025**, it combines **journaling**, **AI-powered insights**, and **beautiful mood analytics** to make self-care engaging and data-driven.

---

## 📸 Screenshots
> *(Coming soon)* – Will showcase the app’s UI, mood boards, and analytics dashboard.

---

## 🚀 Features

| Feature | Description |
| --- | --- |
| 📝 **Mood Journal** | Write daily journal entries and tag emotions using a mood selector (emoji slider or icons). |
| 🤖 **AI Mood Summarization** | Analyze journal entries using AI to extract emotional insights and summarize tone. |
| 🎨 **AI Mood Board Generator** | Create AI-generated visual art reflecting your daily mood. |
| 📈 **Mood Analytics Dashboard** | View emotion trends, a mood calendar, and charts tracking patterns over time. |
| 🔔 **Smart Reminders** | Gentle notifications via OneSignal to encourage daily journaling and mindfulness. |
| 💎 **Premium Tier via RevenueCat** | Unlock unlimited mood boards, timeline export, voice journaling, and custom themes. |

---

## 🏗 Folder Structure

com.mindspace.app/
├── ui/ # Presentation layer
│ ├── screens/ # Screens (HomeScreen, JournalScreen, etc.)
│ └── components/ # Reusable UI elements (MoodSlider, Cards, etc.)
├── viewmodel/ # ViewModels (MVVM architecture)
├── model/ # Data models (Entry.kt, Mood.kt)
├── repository/ # Repository interfaces + implementations
├── utils/ # Utility functions & extensions
├── services/ # External integrations
│ ├── OneSignal/ # Push notification logic
│ ├── RevenueCat/ # Subscription handling
│ └── AI/ # GPT & moodboard generation
├── data/ # Firebase Firestore access
└── MainActivity.kt # App entry point


---

## 🛠 Tech Stack

- **Language:** Kotlin  
- **UI Framework:** Jetpack Compose  
- **Architecture:** MVVM  
- **Backend Services:** Firebase Auth + Firestore  
- **Monetization:** RevenueCat SDK  
- **Push Notifications:** OneSignal SDK  
- **AI Integration:** OpenAI API / Stable Diffusion API  
- **Analytics:** Firebase Analytics  

---

## 🔧 Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/mindspace.git
   cd mindspace
