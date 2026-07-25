package com.example.data

import com.example.R
import com.example.model.*

object MockData {

    val dailyQuotes = listOf(
        "\"You are entirely up to you. Make today radiate with your inner light.\" ✨",
        "\"Nurture your mind, honor your energy, and trust your unique path.\" 🌸",
        "\"Small daily habits lead to extraordinary lifelong transformation.\" 💫",
        "\"Your peace is a priority. Your purpose is a journey.\" 🌿"
    )

    val moodOptions = listOf(
        Pair("🌸", "Radiant"),
        Pair("🌿", "Calm"),
        Pair("💫", "Inspired"),
        Pair("☕", "Cozy"),
        Pair("⚡", "Focused"),
        Pair("☁️", "Pensive")
    )

    val exploreCategories = listOf(
        ExploreCategory(
            id = "beauty",
            title = "Beauty & Skincare",
            subtitle = "Custom glowing routines & glass-skin secrets",
            iconName = "Face",
            description = "Discover personalized skincare layering, ingredient guides (Niacinamide, Retinol, HA), and clean beauty routines crafted for your skin type.",
            suggestedPrompts = listOf(
                "Design a PM skincare routine for sensitive combination skin",
                "How do I layer Hyaluronic Acid, Vitamin C, and Retinol?",
                "Natural morning glow makeup look step-by-step"
            ),
            featuredTip = "Tip: Apply Hyaluronic Acid to slightly damp skin to lock in maximum hydration."
        ),
        ExploreCategory(
            id = "health_edu",
            title = "Health Education",
            subtitle = "Empowering, educational guidance for every stage",
            iconName = "Favorite",
            description = "General educational information regarding cycle phases, hormonal balance, energy optimization, and body wellness. Educational only, non-diagnostic.",
            suggestedPrompts = listOf(
                "Explain the 4 phases of the menstrual cycle and energy levels",
                "What foods help support natural hormone balance during luteal phase?",
                "Gentle morning stretching for lower back relief"
            ),
            featuredTip = "Tip: Tracking your energy levels across cycle phases helps optimize work and workout routines.",
            badge = "Essential"
        ),
        ExploreCategory(
            id = "fitness",
            title = "Fitness & Pilates",
            subtitle = "Low-impact, strength, and mindful movement",
            iconName = "FitnessCenter",
            description = "Tailored workout routines ranging from mat Pilates, posture correction, glute strengthening, to high-energy cardio flows.",
            suggestedPrompts = listOf(
                "20-minute gentle mat Pilates routine for core strength",
                "Post-desk job shoulder and neck posture relief routine",
                "Beginner friendly dumbbell workout plan for women"
            ),
            featuredTip = "Tip: Consistency in low-impact movement builds sustained core stability without high cortisol spike."
        ),
        ExploreCategory(
            id = "nutrition",
            title = "Nutrition & Recipes",
            subtitle = "Nourishing meal ideas & anti-inflammatory recipes",
            iconName = "Restaurant",
            description = "High-protein breakfast ideas, hormone-friendly smoothies, meal prep strategies, and gut health recipes.",
            suggestedPrompts = listOf(
                "3 quick anti-inflammatory breakfast bowls with 25g+ protein",
                "Weekly meal prep guide for busy working professionals",
                "Glowing skin smoothie recipe with collagen and berries"
            ),
            featuredTip = "Tip: Pairing complex carbohydrates with healthy fats and protein stabilizes morning blood sugar."
        ),
        ExploreCategory(
            id = "study",
            title = "Study & Focus",
            subtitle = "Pomodoro techniques, exam prep & memory tools",
            iconName = "School",
            description = "Optimize your learning with active recall, structured study schedules, essay outlines, and deep work strategies.",
            suggestedPrompts = listOf(
                "Create a 4-week structured study schedule for final exams",
                "How do I use Feynman technique to summarize complex topics?",
                "Outline an essay on sustainable innovation and female leadership"
            ),
            featuredTip = "Tip: 50-minute deep work blocks followed by 10-minute screen-free breaks boost long-term retention."
        ),
        ExploreCategory(
            id = "career",
            title = "Career & Growth",
            subtitle = "Negotiation, resume polishing & leadership tips",
            iconName = "Work",
            description = "Navigate career transitions, salary negotiations, elevator pitches, and confident workplace communication.",
            suggestedPrompts = listOf(
                "Help me draft a confident salary negotiation email",
                "How to answer 'Tell me about yourself' in a tech leadership interview",
                "Strategies for overcoming imposter syndrome in a new senior role"
            ),
            featuredTip = "Tip: Document your weekly wins in a 'Brag Sheet' to prepare effortlessly for performance reviews.",
            badge = "Popular"
        ),
        ExploreCategory(
            id = "fashion",
            title = "Fashion & Style",
            subtitle = "Capsule wardrobing & outfit inspiration",
            iconName = "Checkroom",
            description = "Build chic capsule wardrobes, style outfits for seasonal transitions, and find event styling suggestions.",
            suggestedPrompts = listOf(
                "Build a 15-piece Parisian chic summer capsule wardrobe",
                "How to style an oversized blazer for business casual vs evening out",
                "Color palette recommendations for warm autumn undertones"
            ),
            featuredTip = "Tip: Monochrome neutral bases with high-quality statement accessories elevate any minimal outfit."
        ),
        ExploreCategory(
            id = "travel",
            title = "Travel & Solo Trips",
            subtitle = "Curated itineraries & safe solo adventure guides",
            iconName = "Flight",
            description = "Explore safe solo travel destinations, packing lists, hidden local gems, and cultural travel itineraries.",
            suggestedPrompts = listOf(
                "Create a 5-day aesthetic and safe solo travel itinerary for Kyoto",
                "Ultimate carry-on packing list for a 2-week European summer trip",
                "Top safe solo travel spots in South Europe with high walkability"
            ),
            featuredTip = "Tip: Always save offline maps and keep digital copies of essential documents in encrypted cloud storage."
        ),
        ExploreCategory(
            id = "mindfulness",
            title = "Mindfulness & Zen",
            subtitle = "Breathing flows, anxiety relief & grounding rituals",
            iconName = "SelfImprovement",
            description = "Guided box breathing, somatic stress release, evening wind-down rituals, and self-compassion tools.",
            suggestedPrompts = listOf(
                "Guide me through a 4-7-8 breathing exercise to calm anxiety",
                "10-minute bedtime relaxation ritual for deep restorative sleep",
                "Journal prompts for letting go of perfectionism"
            ),
            featuredTip = "Tip: Prolonged exhales activate the parasympathetic nervous system, lowering heart rate instantly."
        ),
        ExploreCategory(
            id = "productivity",
            title = "Time Management",
            subtitle = "Time-blocking, task prioritization & flow state",
            iconName = "Schedule",
            description = "Master Eisenhower Matrix task sorting, morning routine design, and distraction-free workflow systems.",
            suggestedPrompts = listOf(
                "Design a balanced 30-minute high-energy morning routine",
                "How to time-block my workday for maximum creative deep work",
                "System for organizing digital notes, calendar, and daily tasks"
            ),
            featuredTip = "Tip: Limit your primary daily priorities to 3 key outcomes to protect focus and prevent burn-out."
        ),
        ExploreCategory(
            id = "relationships",
            title = "Relationships & Social",
            subtitle = "Boundary setting, active listening & healthy bonds",
            iconName = "People",
            description = "Communicate boundaries clearly, cultivate deep friendships, and navigate family dynamics with empathy.",
            suggestedPrompts = listOf(
                "How to set polite but firm boundary with a overly demanding colleague",
                "Meaningful questions to ask friends to build deeper connections",
                "Communication tips for resolving misunderstandings calmly"
            ),
            featuredTip = "Tip: Use 'I feel' statements instead of 'You always' to maintain psychological safety in conversations."
        ),
        ExploreCategory(
            id = "sleep",
            title = "Sleep & Rest",
            subtitle = "Circadian rhythm alignment & sleep hygiene",
            iconName = "NightsStay",
            description = "Optimizing magnesium intake, screen-free wind-down routines, and natural sleep hygiene practices.",
            suggestedPrompts = listOf(
                "How to reset circadian rhythm after travel or late nights",
                "Non-screen evening activities to prepare for deep sleep",
                "Why morning sunlight exposure improves nighttime sleep quality"
            ),
            featuredTip = "Tip: Getting 10 minutes of direct sunlight within an hour of waking sets your internal sleep clock."
        ),
        ExploreCategory(
            id = "pregnancy_parenting",
            title = "Pregnancy & Parenting",
            subtitle = "Educational guidance & warm general tips",
            iconName = "ChildCare",
            description = "General educational guidance on maternity prep, newborn care basics, self-care for moms, and toddler play ideas.",
            suggestedPrompts = listOf(
                "Essential hospital bag checklist for expecting mothers",
                "Self-care rituals for postpartum mothers during the first month",
                "Montessori-inspired sensory play activities for toddlers"
            ),
            featuredTip = "Tip: Remember that self-care for mothers is essential care for the whole family.",
            badge = "Warm Guide"
        )
    )

    val initialHabits = listOf(
        HabitItem("h1", "Hydration Goal", "Wellness", 6, 8, "glasses", 12, false, "LocalDrink"),
        HabitItem("h2", "Skincare PM", "Beauty", 1, 1, "routine", 21, true, "Face"),
        HabitItem("h3", "Pilates / Stretch", "Fitness", 20, 30, "mins", 5, false, "FitnessCenter"),
        HabitItem("h4", "Daily Journaling", "Mindfulness", 1, 1, "entry", 14, true, "EditNote"),
        HabitItem("h5", "Read 15 Pages", "Study", 12, 15, "pages", 8, false, "MenuBook"),
        HabitItem("h6", "Evening Wind-down", "Sleep", 0, 1, "routine", 4, false, "NightsStay")
    )

    val initialJournalEntries = listOf(
        JournalEntry(
            id = "j1",
            date = "Today, 8:30 AM",
            title = "Morning Clarity & New Aspirations ✨",
            content = "Woke up feeling refreshed after a full 8 hours of sleep. Tried Luna's suggested 5-minute morning breathing routine and brewed fresh jasmine tea. Setting an intention to lead my team presentation with calm confidence today.",
            moodEmoji = "🌸",
            moodLabel = "Radiant",
            tags = listOf("Mindfulness", "MorningRoutine", "Career"),
            photoRes = R.drawable.img_hero_luna_1785002455984
        ),
        JournalEntry(
            id = "j2",
            date = "Yesterday, 9:15 PM",
            title = "Evening Reflection & Gentle Pilates 🌿",
            content = "Completed a 20-minute mat Pilates flow targeting posture. My shoulders feel so much lighter after sitting at my desk all day. Practiced gratitude for small moments of warmth.",
            moodEmoji = "🌿",
            moodLabel = "Calm",
            tags = listOf("Pilates", "SelfCare", "Gratitude")
        ),
        JournalEntry(
            id = "j3",
            date = "July 23, 2026",
            title = "Skincare Discovery & Glass Skin Routine 💫",
            content = "Luna suggested switching to applying my hyaluronic acid serum on damp skin right after cleansing. The difference in skin plumping today was incredible! Really enjoying this personalized routine.",
            moodEmoji = "💫",
            moodLabel = "Inspired",
            tags = listOf("Skincare", "Beauty", "Glow")
        )
    )

    val initialChatHistory = listOf(
        ChatMessage(
            id = "m1",
            sender = MessageSender.LUNA,
            text = "Welcome back, Sophia ✨ I'm Luna, your personal companion for productivity, wellness, beauty, and growth. How can I support your journey today?",
            timestamp = "10:00 AM",
            category = "Greeting"
        ),
        ChatMessage(
            id = "m2",
            sender = MessageSender.USER,
            text = "Hi Luna! Can you suggest a soothing PM skincare routine and a 5-minute wind-down ritual for tonight?",
            timestamp = "10:01 AM"
        ),
        ChatMessage(
            id = "m3",
            sender = MessageSender.LUNA,
            text = "Here is your customized **Evening Glowing Skincare & Wind-Down Ritual** 🌸:\n\n### 1. Gentle PM Skincare Routine\n- **Double Cleanse:** Start with an oil cleanser to melt away makeup & SPF, followed by a gentle hydrating gel cleanser.\n- **Hydrate Damp Skin:** Press 2-3 drops of Hyaluronic Acid into damp face & neck.\n- **Nourish & Seal:** Apply a ceramide barrier cream, finished with a drop of squalane oil.\n\n### 2. 5-Minute Wind-Down Breathing\n- **4-7-8 Breathing Flow:** Inhale through nose for 4s, hold for 7s, exhale slowly through mouth for 8s (repeat 4 cycles).\n- **Digital Sunset:** Place phone in night mode 30 mins before sleep.\n\n*Would you like me to add a reminder to your habit tracker for 9:30 PM?*",
            timestamp = "10:02 AM",
            category = "Beauty & Skincare",
            isLiked = true
        )
    )

    val quickPrompts = listOf(
        "✨ Design my morning glow routine",
        "🧘 5-min stress relief breathing",
        "💼 Confident salary negotiation tips",
        "🥗 High-protein anti-inflammatory meal idea",
        "✈️ 3-day Kyoto solo trip itinerary",
        "📚 Exam deep-work study schedule"
    )

    val initialSavedResponses = listOf(
        SavedResponse(
            id = "s1",
            title = "PM Glass Skin Layering Guide",
            text = "Double cleanse -> HA on damp skin -> Ceramide moisturizer -> Squalane oil seal.",
            category = "Beauty & Skincare",
            savedDate = "Today"
        ),
        SavedResponse(
            id = "s2",
            title = "Confident Workplace Email Template",
            text = "Thank you for sharing this update. Based on market benchmarks and recent milestones...",
            category = "Career",
            savedDate = "Yesterday"
        )
    )

    val initialNotifications = listOf(
        AppNotification("n1", "Hydration Reminder 💧", "Time for your 6th glass of water to keep your skin glowing and energy high!", "20m ago", false, NotificationType.HABIT),
        AppNotification("n2", "Evening Affirmation ✨", "\"Your energy is magnetic when you honor your rest.\" Take 5 minutes to journal.", "2h ago", false, NotificationType.AI_TIP),
        AppNotification("n3", "Weekly Cycle Insight 🌸", "Entering your follicular phase: energy and creativity are naturally peaking this week!", "1d ago", true, NotificationType.WELLNESS)
    )
}
