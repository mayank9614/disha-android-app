package com.disha.career.ui

import com.disha.career.engine.Reason
import com.disha.career.model.Budget
import com.disha.career.model.Lang
import com.disha.career.model.Stage
import com.disha.career.model.Stream

/**
 * UI text for both languages. Kept in Kotlin rather than strings.xml so the
 * language toggle switches instantly without restarting the activity.
 */
class Strings(
    val appName: String,
    val tagline: String,
    val privacyNote: String,
    val language: String,
    val startTest: String,
    val seeLastResult: String,
    val profileTitle: String,
    val qClass: String,
    val class10: String,
    val class12: String,
    val qStream: String,
    val streamNames: Map<Stream, String>,
    val qBudget: String,
    val budgetNames: Map<Budget, String>,
    val qRelocate: String,
    val qGovt: String,
    val yes: String,
    val no: String,
    val continueBtn: String,
    val sectionInterest: String,
    val sectionAptitude: String,
    val questionOf: (current: Int, total: Int) -> String,
    /** Answer labels for interest questions, matching LIKERT_POINTS order. */
    val likert: List<String>,
    val resultTitle: String,
    val personaHeadline: (name: String) -> String,
    val personaNames: Map<String, String>,
    val personaDesc: Map<String, String>,
    val topMatches: String,
    val matchLabel: (score: Int) -> String,
    val aiProofLabel: (score: Int) -> String,
    val whyFits: String,
    val lockedTitle: String,
    val lockedPoints: List<String>,
    val unlockFor: (price: String) -> String,
    val paymentUnavailable: String,
    val openReport: String,
    val share: String,
    val shareText: (persona: String, career: String, score: Int, link: String) -> String,
    val retake: String,
    val noMatches: String,
    val reportTitle: String,
    val roadmap: String,
    val exams: String,
    val salary: String,
    val skills: String,
    val aiFuture: String,
    val govtTag: String,
    val lowCostTag: String,
    val nearHomeTag: String,
    val reasonText: (Reason) -> String,
    val planTitle: String,
    val plan: Map<Stage, List<String>>,
    val disclaimer: String,
)

val PERSONA_EMOJI = mapOf("R" to "🛠️", "I" to "🔬", "A" to "🎨", "S" to "🤝", "E" to "🚀", "C" to "📊")

/** Used when Play hasn't returned the real price yet. Keep in sync with Play Console. */
const val FALLBACK_PRICE = "₹9"

fun stringsFor(lang: Lang): Strings = if (lang == Lang.HI) HINDI else ENGLISH

private val ENGLISH = Strings(
    appName = "Disha",
    tagline = "Find your direction after 10th & 12th",
    privacyNote = "Works offline. Your answers never leave your phone.",
    language = "Language",
    startTest = "Start free test (10 min)",
    seeLastResult = "See my last result",
    profileTitle = "Tell us about yourself",
    qClass = "Which class have you passed?",
    class10 = "10th",
    class12 = "12th",
    qStream = "Your stream in 12th",
    streamNames = mapOf(Stream.SCIENCE to "Science", Stream.COMMERCE to "Commerce", Stream.ARTS to "Arts"),
    qBudget = "How much can your family spend on studies per year?",
    budgetNames = mapOf(
        Budget.LOW to "Up to ₹20,000",
        Budget.MEDIUM to "₹20,000 – ₹1 lakh",
        Budget.HIGH to "More than ₹1 lakh",
    ),
    qRelocate = "Are you ready to move to another city for study or work?",
    qGovt = "Do you want a government job?",
    yes = "Yes",
    no = "No",
    continueBtn = "Continue",
    sectionInterest = "What do you enjoy?",
    sectionAptitude = "Brain teaser",
    questionOf = { current, total -> "Question $current of $total" },
    likert = listOf("Love it 😍", "It's okay 🙂", "Not for me 🙅"),
    resultTitle = "Your result",
    personaHeadline = { "You are a $it" },
    personaNames = mapOf(
        "R" to "Builder", "I" to "Thinker", "A" to "Creator",
        "S" to "Helper", "E" to "Leader", "C" to "Organiser",
    ),
    personaDesc = mapOf(
        "R" to "You learn by doing. Practical, hands-on work where you can see results is your strength.",
        "I" to "You're curious and love solving problems. You ask \"why\" and \"how\" more than most.",
        "A" to "You see the world in your own way. Creating, designing and expressing ideas energise you.",
        "S" to "People trust you. Helping, teaching and caring for others comes naturally to you.",
        "E" to "You like to take charge and make things happen. Business and leadership suit you.",
        "C" to "You're reliable and organised. You bring order where others see confusion.",
    ),
    topMatches = "Your top 3 career matches",
    matchLabel = { "$it% match" },
    aiProofLabel = { "AI-proof score: $it/100" },
    whyFits = "Why it fits you",
    lockedTitle = "Unlock your full report",
    lockedPoints = listOf(
        "All your matching careers, not just 3",
        "Step-by-step roadmap for each career",
        "Entrance exams, costs and earnings",
        "Your personal 6-month action plan",
    ),
    unlockFor = { "Unlock for $it" },
    paymentUnavailable = "Payments aren't available right now. Check that Play Store is installed and try again.",
    openReport = "Open full report",
    share = "Share with friends",
    shareText = { persona, career, score, link ->
        "I took the Disha career test and I'm a $persona! My top match: $career ($score%). Find your direction too: $link"
    },
    retake = "Retake test",
    noMatches = "No careers matched your profile. Try retaking the test with different choices.",
    reportTitle = "Your full career report",
    roadmap = "Roadmap",
    exams = "Exams & admissions",
    salary = "Earnings",
    skills = "Skills to build",
    aiFuture = "In the AI age",
    govtTag = "Govt job",
    lowCostTag = "Low cost",
    nearHomeTag = "Possible near home",
    reasonText = { reason ->
        when (reason) {
            is Reason.Interest -> when (reason.dim) {
                "R" -> "You enjoy hands-on, practical work."
                "I" -> "You love figuring out how things work."
                "A" -> "You have a creative streak."
                "S" -> "You care about helping people."
                "E" -> "You like to lead and take initiative."
                else -> "You're organised and pay attention to detail."
            }
            is Reason.Aptitude -> when (reason.dim) {
                "NUM" -> "You did well on the number questions."
                "VERB" -> "You did well on the language questions."
                else -> "You did well on the reasoning questions."
            }
            Reason.GovtMatch -> "It offers the government job you want."
            Reason.LowCost -> "It can be started on a small budget."
            Reason.NearHome -> "You can do it without leaving your area."
            Reason.BudgetStretch -> "It may cost more than your budget, so look for scholarships."
            Reason.NeedsMove -> "You may need to move to a bigger city."
        }
    },
    planTitle = "Your 6-month action plan",
    plan = mapOf(
        Stage.AFTER_10 to listOf(
            "Month 1: Talk to your parents and one teacher about your top 2 careers from this report.",
            "Month 1–2: Choose your path: 11th stream (Science / Commerce / Arts), ITI or polytechnic. Check admission dates.",
            "Month 2–4: Meet one person already working in your top career. Ask what their day looks like.",
            "Month 4–6: Start one free skill course online, then take this test again to see what changed.",
        ),
        Stage.AFTER_12 to listOf(
            "Month 1: Shortlist 2 careers from this report and discuss them with your family.",
            "Month 1–2: Note the entrance exams and form dates for those careers; many forms open soon after results.",
            "Month 2–4: Apply for courses or start exam preparation with a fixed daily timetable.",
            "Month 4–6: Learn one practical skill alongside (computer, spoken English or a trade) as your backup.",
        ),
    ),
    disclaimer = "This report is guidance, not a final decision. Fees, earnings and exam details are approximate and change often, so always check official websites and talk to your parents and teachers.",
)

private val HINDI = Strings(
    appName = "दिशा",
    tagline = "10वीं और 12वीं के बाद अपनी सही दिशा पाएँ",
    privacyNote = "बिना इंटरनेट चलता है। आपके जवाब आपके फ़ोन से बाहर नहीं जाते।",
    language = "भाषा",
    startTest = "मुफ़्त टेस्ट शुरू करें (10 मिनट)",
    seeLastResult = "मेरा पिछला रिज़ल्ट देखें",
    profileTitle = "अपने बारे में बताएँ",
    qClass = "आपने कौन-सी कक्षा पास की है?",
    class10 = "10वीं",
    class12 = "12वीं",
    qStream = "12वीं में आपकी स्ट्रीम",
    streamNames = mapOf(Stream.SCIENCE to "साइंस", Stream.COMMERCE to "कॉमर्स", Stream.ARTS to "आर्ट्स (कला)"),
    qBudget = "आपका परिवार पढ़ाई पर साल में कितना ख़र्च कर सकता है?",
    budgetNames = mapOf(
        Budget.LOW to "₹20,000 तक",
        Budget.MEDIUM to "₹20,000 – ₹1 लाख",
        Budget.HIGH to "₹1 लाख से ज़्यादा",
    ),
    qRelocate = "क्या आप पढ़ाई या काम के लिए दूसरे शहर जाने को तैयार हैं?",
    qGovt = "क्या आप सरकारी नौकरी चाहते हैं?",
    yes = "हाँ",
    no = "नहीं",
    continueBtn = "आगे बढ़ें",
    sectionInterest = "आपको क्या पसंद है?",
    sectionAptitude = "दिमाग़ी कसरत",
    questionOf = { current, total -> "सवाल $current / $total" },
    likert = listOf("बहुत पसंद 😍", "ठीक-ठाक 🙂", "मेरे लिए नहीं 🙅"),
    resultTitle = "आपका रिज़ल्ट",
    personaHeadline = { "आप हैं: $it" },
    personaNames = mapOf(
        "R" to "निर्माता", "I" to "खोजी", "A" to "रचनाकार",
        "S" to "सहयोगी", "E" to "लीडर", "C" to "व्यवस्थापक",
    ),
    personaDesc = mapOf(
        "R" to "आप करके सीखते हैं। व्यावहारिक, हाथ से किया जाने वाला काम, जिसका नतीजा दिखे, आपकी ताक़त है।",
        "I" to "आप जिज्ञासु हैं और समस्याएँ हल करना पसंद करते हैं। आप दूसरों से ज़्यादा \"क्यों\" और \"कैसे\" पूछते हैं।",
        "A" to "आप दुनिया को अपने अलग अंदाज़ में देखते हैं। कुछ नया बनाना, डिज़ाइन करना और अपने विचार ज़ाहिर करना आपको ऊर्जा देता है।",
        "S" to "लोग आप पर भरोसा करते हैं। मदद करना, पढ़ाना और दूसरों की देखभाल करना आपको सहज आता है।",
        "E" to "आपको ज़िम्मेदारी लेना और काम करके दिखाना पसंद है। बिज़नेस और नेतृत्व आप पर जँचते हैं।",
        "C" to "आप भरोसेमंद और व्यवस्थित हैं। जहाँ दूसरों को उलझन दिखती है, वहाँ आप व्यवस्था लाते हैं।",
    ),
    topMatches = "आपके लिए टॉप 3 करियर",
    matchLabel = { "$it% मेल" },
    aiProofLabel = { "AI-सुरक्षा स्कोर: $it/100" },
    whyFits = "यह आपके लिए क्यों सही है",
    lockedTitle = "अपनी पूरी रिपोर्ट अनलॉक करें",
    lockedPoints = listOf(
        "सिर्फ़ 3 नहीं, आपसे मेल खाने वाले सभी करियर",
        "हर करियर का स्टेप-बाय-स्टेप रास्ता",
        "प्रवेश परीक्षाएँ, ख़र्च और कमाई",
        "आपका 6 महीने का एक्शन प्लान",
    ),
    unlockFor = { "$it में अनलॉक करें" },
    paymentUnavailable = "अभी पेमेंट उपलब्ध नहीं है। देखें कि Play Store इंस्टॉल है और फिर कोशिश करें।",
    openReport = "पूरी रिपोर्ट खोलें",
    share = "दोस्तों को भेजें",
    shareText = { persona, career, score, link ->
        "मैंने दिशा करियर टेस्ट दिया, मैं हूँ $persona! मेरा टॉप करियर: $career ($score%)। आप भी अपनी दिशा जानें: $link"
    },
    retake = "टेस्ट फिर से दें",
    noMatches = "आपकी प्रोफ़ाइल से कोई करियर मेल नहीं खाया। अलग विकल्पों के साथ टेस्ट फिर से दें।",
    reportTitle = "आपकी पूरी करियर रिपोर्ट",
    roadmap = "रास्ता (रोडमैप)",
    exams = "परीक्षाएँ और प्रवेश",
    salary = "कमाई",
    skills = "सीखने वाली स्किल्स",
    aiFuture = "AI के दौर में",
    govtTag = "सरकारी नौकरी",
    lowCostTag = "कम ख़र्च",
    nearHomeTag = "घर के पास संभव",
    reasonText = { reason ->
        when (reason) {
            is Reason.Interest -> when (reason.dim) {
                "R" -> "आपको हाथ से, व्यावहारिक काम करना पसंद है।"
                "I" -> "आपको यह समझना पसंद है कि चीज़ें कैसे काम करती हैं।"
                "A" -> "आपमें रचनात्मकता है।"
                "S" -> "आपको लोगों की मदद करना अच्छा लगता है।"
                "E" -> "आपको आगे बढ़कर नेतृत्व करना पसंद है।"
                else -> "आप व्यवस्थित हैं और बारीकियों पर ध्यान देते हैं।"
            }
            is Reason.Aptitude -> when (reason.dim) {
                "NUM" -> "संख्या वाले सवालों में आपने अच्छा किया।"
                "VERB" -> "भाषा वाले सवालों में आपने अच्छा किया।"
                else -> "तर्क (रीज़निंग) वाले सवालों में आपने अच्छा किया।"
            }
            Reason.GovtMatch -> "इसमें आपकी चाही सरकारी नौकरी का मौक़ा है।"
            Reason.LowCost -> "इसे कम बजट में शुरू किया जा सकता है।"
            Reason.NearHome -> "इसके लिए अपना इलाक़ा छोड़ना ज़रूरी नहीं।"
            Reason.BudgetStretch -> "इसमें आपके बजट से ज़्यादा ख़र्च हो सकता है, इसलिए स्कॉलरशिप देखें।"
            Reason.NeedsMove -> "आपको किसी बड़े शहर जाना पड़ सकता है।"
        }
    },
    planTitle = "आपका 6 महीने का एक्शन प्लान",
    plan = mapOf(
        Stage.AFTER_10 to listOf(
            "महीना 1: इस रिपोर्ट के अपने टॉप 2 करियर के बारे में माता-पिता और किसी एक शिक्षक से बात करें।",
            "महीना 1–2: अपना रास्ता चुनें: 11वीं की स्ट्रीम (साइंस / कॉमर्स / आर्ट्स), ITI या पॉलिटेक्निक। प्रवेश की तारीख़ें देखें।",
            "महीना 2–4: अपने टॉप करियर में काम कर रहे किसी एक व्यक्ति से मिलें। पूछें कि उनका दिन कैसा होता है।",
            "महीना 4–6: एक मुफ़्त ऑनलाइन स्किल कोर्स शुरू करें, फिर यह टेस्ट दोबारा देकर देखें कि क्या बदला।",
        ),
        Stage.AFTER_12 to listOf(
            "महीना 1: इस रिपोर्ट से 2 करियर चुनें और परिवार के साथ उन पर बात करें।",
            "महीना 1–2: उन करियर की प्रवेश परीक्षाएँ और फ़ॉर्म की तारीख़ें नोट करें; कई फ़ॉर्म रिज़ल्ट के तुरंत बाद खुलते हैं।",
            "महीना 2–4: कोर्स के लिए आवेदन करें या रोज़ के तय टाइमटेबल के साथ परीक्षा की तैयारी शुरू करें।",
            "महीना 4–6: बैकअप के तौर पर साथ में एक व्यावहारिक स्किल सीखें (कंप्यूटर, अंग्रेज़ी बोलना या कोई हुनर)।",
        ),
    ),
    disclaimer = "यह रिपोर्ट सिर्फ़ मार्गदर्शन है, आख़िरी फ़ैसला नहीं। फ़ीस, कमाई और परीक्षा की जानकारी अनुमानित है और बदलती रहती है, इसलिए हमेशा आधिकारिक वेबसाइट देखें और माता-पिता व शिक्षकों से बात करें।",
)
