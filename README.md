# Disha: career guidance after 10th & 12th

An offline Android app (Kotlin + Jetpack Compose) for students in UP, Bihar and other
northern states. Students take a ~10-minute test in Hindi or English, get a free result
(their personality type plus their top 3 career matches), and can pay a small one-time
amount to unlock the full report.

**There's no server, no database and no paid API.** Questions, careers and scoring all ship
inside the APK, and answers are stored only on the phone. The app doesn't even ask for the
INTERNET permission.

## How it works

| Part | File |
| --- | --- |
| Questions (18 interest + 6 aptitude), Hindi and English | `app/src/main/assets/questions.json` |
| 13 careers with roadmap, exams, earnings, AI outlook | `app/src/main/assets/careers.json` |
| Scoring engine | `app/src/main/java/com/disha/career/engine/ScoringEngine.kt` |
| All UI text in both languages | `app/src/main/java/com/disha/career/ui/L10n.kt` |
| Google Play one-time unlock | `app/src/main/java/com/disha/career/billing/BillingManager.kt` |

**Scoring:** each career gets a match score from three parts:
- **60% interest fit:** how closely the student's interest answers (Builder, Thinker, Creator, Helper, Leader, Organiser) match the career's interest weights.
- **25% aptitude:** results on the number, language and reasoning questions.
- **15% real-life fit:** family budget, and whether the student can move to another city.

Careers the student can't enter yet (wrong class or stream) are filtered out. Students who
want a government job get a small bonus on government careers.

**Free vs paid:**
- **Free:** the personality type, the top 3 matches with their AI-proof scores, and reasons for the #1 match.
- **Paid:** up to 8 careers, each with a full roadmap, exams, earnings, skills and AI outlook, plus a 6-month action plan.

To add or edit content, change the JSON files and run the tests. No code change is needed.

## Run it

1. Install [Android Studio](https://developer.android.com/studio). It includes the JDK and the Android SDK.
2. Open this folder in Android Studio and let Gradle sync. It downloads Gradle 8.9 and the libraries.
3. Press Run, on a phone with USB debugging or on an emulator.
4. Debug builds show an **Unlock (debug build only)** button, so you can see the paid report without setting up Play billing.

Run the scoring tests:

```bash
./gradlew test
```

If Android Studio complains that the Gradle wrapper JAR is missing, run `gradle wrapper`
once, or use **File → Sync Project with Gradle Files**.

## Before publishing on Play Store

1. Create a Play Console developer account (one-time US$25).
2. Create an in-app product with ID **`full_report`** (a one-time product, not a subscription).
3. Set its price. Check the lowest INR price Play allows; ₹9 is the planned price. If you change it, update `FALLBACK_PRICE` in `L10n.kt`.
4. Add your Gmail as a license tester, then upload a build to the internal testing track. Real purchase flows only work on builds installed from Play.
5. Replace the placeholder icon in `res/drawable/ic_launcher.xml`. The app name "Disha" and the package name `com.disha.career` are placeholders too: check that they aren't already taken.

**Things to know about payments:**
- **Google's cut:** Play keeps about 15% of each sale.
- **Verification:** purchases are checked on the phone, without a server. That's easier to crack than server checks, but reasonable for a ₹9 unlock.
- **Unacknowledged purchases:** Play refunds any purchase not acknowledged within 3 days. `BillingManager` acknowledges them automatically.

## Content caution

Earnings, fees and exam names are approximate and change every year. Review
`careers.json` before each admission season, and keep the in-app disclaimer.
