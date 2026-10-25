package com.example.data.local

import com.example.data.local.entity.CategoryEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.SystemSettingsEntity
import com.example.data.local.entity.UserEntity

object SeedDataProvider {

    fun getInitialUsers(): List<UserEntity> = listOf(
        UserEntity(
            id = "user-manager-01",
            employeeNumber = "MGR-01",
            name = "أحمد الجزار (الشيف العمومي)",
            role = "manager",
            pin = "1234",
            phone = "+201000000000",
            assignedCategoriesCsv = "cat-veg,cat-meat,cat-poultry,cat-spices,cat-sauces,cat-grocery,cat-dairy,cat-frozen-oils,cat-bread,cat-packaging,cat-cleaning,cat-cashier"
        ),
        UserEntity(
            id = "user-assistant-01",
            employeeNumber = "AST-01",
            name = "محمد علي (مساعد 1 - خضار ولحوم)",
            role = "assistant",
            pin = "1111",
            phone = "+201111111111",
            assignedCategoriesCsv = "cat-veg,cat-meat"
        ),
        UserEntity(
            id = "user-assistant-02",
            employeeNumber = "AST-02",
            name = "كريم صبري (مساعد 2 - دواجن ومجمدات)",
            role = "assistant",
            pin = "2222",
            phone = "+201222222222",
            assignedCategoriesCsv = "cat-poultry,cat-frozen-oils"
        ),
        UserEntity(
            id = "user-assistant-03",
            employeeNumber = "AST-03",
            name = "ياسر محمود (مساعد 3 - توابل وصوصات)",
            role = "assistant",
            pin = "3333",
            phone = "+201333333333",
            assignedCategoriesCsv = "cat-spices,cat-sauces"
        ),
        UserEntity(
            id = "user-assistant-04",
            employeeNumber = "AST-04",
            name = "طارق حامد (مساعد 4 - بقالة وألبان ومخبوزات)",
            role = "assistant",
            pin = "4444",
            phone = "+201444444444",
            assignedCategoriesCsv = "cat-grocery,cat-dairy,cat-bread"
        ),
        UserEntity(
            id = "user-assistant-05",
            employeeNumber = "AST-05",
            name = "مصطفى إبراهيم (مساعد 5 - تغليف ونظافة وكاشير)",
            role = "assistant",
            pin = "5555",
            phone = "+201555555555",
            assignedCategoriesCsv = "cat-packaging,cat-cleaning,cat-cashier"
        ),
        UserEntity(
            id = "user-receiver-01",
            employeeNumber = "REC-01",
            name = "حسام عبد الله (مسؤول الاستلام وضبط الجودة)",
            role = "receiver",
            pin = "9999",
            phone = "+201666666666",
            assignedCategoriesCsv = ""
        )
    )

    fun getInitialCategories(): List<CategoryEntity> = listOf(
        CategoryEntity(
            id = "cat-veg",
            code = "VEG",
            nameAr = "خضار وسلطات طازجة",
            nameEn = "Vegetables & Produce",
            icon = "🥬",
            recurrence = "daily",
            scheduleTiming = "يومياً 06:00 صباحاً",
            defaultLeadTimeHours = 12,
            defaultAssigneeId = "user-assistant-01",
            defaultSupplierPhone = "+201011122233",
            sheetColumn = 1
        ),
        CategoryEntity(
            id = "cat-meat",
            code = "MEAT",
            nameAr = "لحوم بلدي ومستوردة",
            nameEn = "Meats & Cuts",
            icon = "🥩",
            recurrence = "weekly",
            scheduleTiming = "الأحد 08:00 صباحاً",
            defaultLeadTimeHours = 24,
            defaultAssigneeId = "user-assistant-01",
            defaultSupplierPhone = "+201022233344",
            sheetColumn = 2
        ),
        CategoryEntity(
            id = "cat-poultry",
            code = "POULTRY",
            nameAr = "دواجن وفراخ فريش",
            nameEn = "Fresh Poultry",
            icon = "🍗",
            recurrence = "daily",
            scheduleTiming = "يومياً 07:30 صباحاً",
            defaultLeadTimeHours = 12,
            defaultAssigneeId = "user-assistant-02",
            defaultSupplierPhone = "+201055566677",
            sheetColumn = 3
        ),
        CategoryEntity(
            id = "cat-spices",
            code = "SPICES",
            nameAr = "بهارات وتوابل شرقية",
            nameEn = "Spices & Seasoning",
            icon = "🧂",
            recurrence = "weekly",
            scheduleTiming = "الأحد 09:00 صباحاً",
            defaultLeadTimeHours = 24,
            defaultAssigneeId = "user-assistant-03",
            defaultSupplierPhone = "+201044455566",
            sheetColumn = 4
        ),
        CategoryEntity(
            id = "cat-sauces",
            code = "SAUCES",
            nameAr = "صوصات وزيوت وخل",
            nameEn = "Sauces & Condiments",
            icon = "🥫",
            recurrence = "weekly",
            scheduleTiming = "الأحد 09:30 صباحاً",
            defaultLeadTimeHours = 24,
            defaultAssigneeId = "user-assistant-03",
            defaultSupplierPhone = "+201044455566",
            sheetColumn = 5
        ),
        CategoryEntity(
            id = "cat-grocery",
            code = "GROCERY",
            nameAr = "بقالة جافة وحبوب",
            nameEn = "Dry Grocery & Grains",
            icon = "🌾",
            recurrence = "weekly",
            scheduleTiming = "الأحد 10:00 صباحاً",
            defaultLeadTimeHours = 24,
            defaultAssigneeId = "user-assistant-04",
            defaultSupplierPhone = "+201088899900",
            sheetColumn = 6
        ),
        CategoryEntity(
            id = "cat-dairy",
            code = "DAIRY",
            nameAr = "ألبان وأجبان وزبدة",
            nameEn = "Dairy & Cheeses",
            icon = "🧀",
            recurrence = "weekly",
            scheduleTiming = "الأحد 10:30 صباحاً",
            defaultLeadTimeHours = 24,
            defaultAssigneeId = "user-assistant-04",
            defaultSupplierPhone = "+201077788899",
            sheetColumn = 7
        ),
        CategoryEntity(
            id = "cat-frozen-oils",
            code = "FROZEN",
            nameAr = "مجمدات وخضار مجمد",
            nameEn = "Frozen Foods & Veg",
            icon = "🧊",
            recurrence = "weekly",
            scheduleTiming = "الأحد 11:00 صباحاً",
            defaultLeadTimeHours = 48,
            defaultAssigneeId = "user-assistant-02",
            defaultSupplierPhone = "+201088899900",
            sheetColumn = 8
        ),
        CategoryEntity(
            id = "cat-bread",
            code = "BREAD",
            nameAr = "مخبوزات وعيش فلاحي وبريوش",
            nameEn = "Bakery & Buns",
            icon = "🥖",
            recurrence = "daily",
            scheduleTiming = "يومياً 06:30 صباحاً",
            defaultLeadTimeHours = 6,
            defaultAssigneeId = "user-assistant-04",
            defaultSupplierPhone = "+201033344455",
            sheetColumn = 9
        ),
        CategoryEntity(
            id = "cat-packaging",
            code = "PACK",
            nameAr = "تغليف وعلب واسترتش",
            nameEn = "Packaging & Disposables",
            icon = "📦",
            recurrence = "monthly",
            scheduleTiming = "أول كل شهر",
            defaultLeadTimeHours = 48,
            defaultAssigneeId = "user-assistant-05",
            defaultSupplierPhone = "+201099900011",
            sheetColumn = 10
        ),
        CategoryEntity(
            id = "cat-cleaning",
            code = "CLEAN",
            nameAr = "أدوات ومواد نظافة",
            nameEn = "Sanitation & Cleaning",
            icon = "🧼",
            recurrence = "monthly",
            scheduleTiming = "أول كل شهر",
            defaultLeadTimeHours = 48,
            defaultAssigneeId = "user-assistant-05",
            defaultSupplierPhone = "+201055544433",
            sheetColumn = 11
        ),
        CategoryEntity(
            id = "cat-cashier",
            code = "CASHIER",
            nameAr = "مستلزمات كاشير ومكتبية",
            nameEn = "Cashier & Stationery",
            icon = "🧾",
            recurrence = "weekly",
            scheduleTiming = "الأحد 11:30 صباحاً",
            defaultLeadTimeHours = 24,
            defaultAssigneeId = "user-assistant-05",
            defaultSupplierPhone = "+201088877766",
            sheetColumn = 12
        )
    )

    fun getInitialProducts(): List<ProductEntity> {
        val list = mutableListOf<ProductEntity>()
        var globalIndex = 1

        fun add(catId: String, name: String, unit: String, par: Double, col: Int, row: Int) {
            list.add(
                ProductEntity(
                    id = "prod-$catId-${list.size + 1}",
                    categoryId = catId,
                    nameAr = name,
                    unit = unit,
                    parLevel = par,
                    sheetColumn = col,
                    sheetRow = row,
                    isActive = true
                )
            )
            globalIndex++
        }

        // 1. الخضار (25 صنف)
        val vegItems = listOf(
            Triple("طماطم بلدي فرز أول", "كجم", 15.0),
            Triple("خيار بلدي صوبة", "كجم", 15.0),
            Triple("خس كابوتشا مدور", "حبة", 15.0),
            Triple("بصل أحمر كشري", "كجم", 40.0),
            Triple("بصل أبيض مستورد", "كجم", 35.0),
            Triple("بطاطس تحمير سيلفر", "كجم", 50.0),
            Triple("فلفل رومي أخضر بلدي", "كجم", 25.0),
            Triple("فلفل حامي شطة أخضر", "كجم", 25.0),
            Triple("ليمون بلدي مياه", "كجم", 20.0),
            Triple("جزر أصفر مسكر", "كجم", 15.0),
            Triple("كوسة فلاحي وسط", "كجم", 25.0),
            Triple("باذنجان رومي حشو", "كجم", 30.0),
            Triple("باذنجان عروس مخلل", "كجم", 45.0),
            Triple("شبت وبقدونس وكزبرة مشكل", "ربطة", 10.0),
            Triple("بصل أخضر بلدي", "ربطة", 20.0),
            Triple("نعناع أخضر فريش", "ربطة", 15.0),
            Triple("كرنب سلطة أحمر مدور", "حبة", 15.0),
            Triple("كرنب سلطة أبيض", "حبة", 10.0),
            Triple("كرفس بلدي للشوربة", "ربطة", 20.0),
            Triple("زنجبيل فريش هندي", "كجم", 12.0),
            Triple("ثوم مقشر حبات أبيض", "كجم", 15.0),
            Triple("فلفل ألوان أصفر وأحمر", "كجم", 20.0),
            Triple("بطاطا مشوية سكرية", "كجم", 15.0),
            Triple("بنجر أحمر للسلطة", "كجم", 25.0),
            Triple("مشروم أبيض طازج كاسات", "طبق", 20.0)
        )
        vegItems.forEachIndexed { i, item -> add("cat-veg", item.first, item.second, item.third, 1, i + 1) }

        // 2. اللحوم (10 أصناف)
        val meatItems = listOf(
            Triple("كندوز قطع توب سايد", "كجم", 25.0),
            Triple("لحم مفروم بلدي أحمر 85/15", "كجم", 25.0),
            Triple("كفتة سيخ جاهزة التتبيل", "كجم", 30.0),
            Triple("عرق فلتو بقرى بلدي", "كجم", 20.0),
            Triple("ستيك ريب آي معتق", "كجم", 15.0),
            Triple("موزة بتلو بلدي بالعظم", "كجم", 8.0),
            Triple("إنتركوت بلدي مشوي", "كجم", 15.0),
            Triple("كبدة إسكندراني شرايح", "كجم", 10.0),
            Triple("كتف ضاني بلدي", "كجم", 10.0),
            Triple("سجق شرقي بلدي مخصوص", "كجم", 20.0)
        )
        meatItems.forEachIndexed { i, item -> add("cat-meat", item.first, item.second, item.third, 2, i + 1) }

        // 3. الدواجن (6 أصناف)
        val poultryItems = listOf(
            Triple("صدور دجاج مخلية بانيه فريش", "كجم", 30.0),
            Triple("فراخ شواية كاملة وزن 1100جم", "فرخة", 35.0),
            Triple("أوراك دجاج شيش طاووق مخلية", "كجم", 25.0),
            Triple("أجنحة دجاج مقرمشة كرسبي", "كجم", 20.0),
            Triple("كبد وقوانص فراخ فريش", "كجم", 25.0),
            Triple("شاورما دجاج متبلة جاهزة", "كجم", 15.0)
        )
        poultryItems.forEachIndexed { i, item -> add("cat-poultry", item.first, item.second, item.third, 3, i + 1) }

        // 4. البهارات والتوابل (34 صنف)
        val spiceNames = listOf(
            "ملح طعام بحري ناعم 1كجم", "ملح طعام خشن صخري", "فلفل أسود حصى برازيلي", "فلفل أسود ناعم نخب أول",
            "كمون بلدي ناعم مطحون", "كمون بلدي حصى منقى", "كزبرة جافة مطحونة خضراء", "كزبرة حصى ناشفة",
            "بابريكا مدخنة إسباني", "قرفة عيدان خشب سيجار", "قرفة سيلان ناعمة", "حبهان هيل أخضر هندي جامبو",
            "حبهان هيل مطحون سادة", "ورق لورا غار سليم تركي", "شطة سوداني ناعمة حارة", "بهارات سبع حبات شيف",
            "كاري مدراس هندي أصلي", "كركم أصفر بودرة هندي", "زنجبيل مطحون سادة", "بهارات كفتة مشوية جاهزة",
            "جوزة الطيب حبات سليمة", "قرنفل مسمار هندي", "سماق بلدي حامض أحمر", "زعتر أوريجانو جاف",
            "ريحان إيطالي جاف مجروش", "فلفل أبيض ناعم مطحون", "ثوم بودرة فاخر ناعم", "بصل بودرة فاخر ناعم",
            "مستكة يوناني درجة أولى", "شمر منقى للشوربات", "ينسون نجمي ياباني", "سماق تركي ناعم",
            "تتبيلة فراخ تندوري شيف", "مرقة دجاج فورية بودرة"
        )
        spiceNames.forEachIndexed { i, name -> add("cat-spices", name, "كجم", if (i in 0..3) 5.0 else 2.0, 4, i + 1) }

        // 5. الصوصات والزيوت (21 صنف)
        val sauceItems = listOf(
            "زيت ذرة كريستال نقي 18 لتر", "زيت عباد شمس عافية 18 لتر", "زيت زيتون بكر ممتاز يوناني",
            "خل أبيض نقي تركيز 5%", "مسطردة هاينز صفراء ديجون", "مايونيز هاينز جالون 4كجم",
            "كاتشب هاينز جالون 4كجم", "صويا صوص داكن ثقيل", "باربيكيو صوص مدخن أمريكي",
            "دبس رمان يماني ممتاز مركز", "صلصة طماطم هاينز معجون 3كجم", "سيراتشا هوت صوص حار",
            "طحينة سمسم بيضاء فاخرة 5كجم", "سويت تشيلي صوص تايلاندي", "صلصة ورشستر شير أصلية",
            "تتبيلة رانش صوص جاهزة", "صوص شيدر سائل للبرجر", "عسل أسود صعيدي نقي",
            "عسل نحل زهور أبيض نقي", "صوص بشاميل فرنسي جاهز", "صوص ديمي جلاس بودرة كنور"
        )
        sauceItems.forEachIndexed { i, name -> add("cat-sauces", name, if (name.contains("لتر")) "لتر" else if (name.contains("جالون") || name.contains("كجم")) "عبوة" else "زجاجة", 6.0, 5, i + 1) }

        // 6. البقالة الجافة والحبوب (14 صنف)
        val groceryItems = listOf(
            "أرز بسمتي هندي سيلا ذهبي حبة طويلة", "أرز مصري حبة رفيعة كسر زيرو", "مكرونة قلم إيطاليانو كيس 10كجم",
            "مكرونة سباجيتي رفيعة 10كجم", "دقيق فاخر استخراج 72% متعدد الأغراض", "نشا ذرة نقي سريع الذوبان",
            "سكر أبيض ناعم مكرر 50كجم", "فول بلدي تدميس حبة عريضة", "عدس أصفر تركي منقى",
            "حمص شام حبة جامبو درجة أولى", "برغل ناعم تركي للكبة", "شعرية تحمير ممتازة",
            "لسان عصفور فاخر للشوربة", "مسحوق كاكاو خام غير محلى"
        )
        groceryItems.forEachIndexed { i, name -> add("cat-grocery", name, if (name.contains("50كجم") || name.contains("10كجم")) "شيكارة" else "كجم", 12.0, 6, i + 1) }

        // 7. الألبان والأجبان (14 صنف)
        val dairyItems = listOf(
            "جبنة موتزاريلا طبيعي مبشورة 100%", "جبنة شيدر أحمر إنجليزي مكعبات", "جبنة رومي قديمة مبشورة",
            "زبدة نيوزيلندي طبيعي غير مملحة", "حليب طازج كامل الدسم بسترة 1لتر", "كريمة خفق وحلويات لبانية 1لتر",
            "كريمة طهي ومكرونات كاملة الدسم", "جبنة فيتا يوناني دمياطي ملح خفيف", "جبنة كيري مربعات كريمية",
            "قشطة بلدي طبيعي طازجة", "جبنة بارميزان إيطالي ريجيانو مبشور", "جبنة جودة هولندي شرائح للبرجر",
            "لبن رايب بلدي مهضوم", "جبنة حلوم قبرصي للشوي"
        )
        dairyItems.forEachIndexed { i, name -> add("cat-dairy", name, if (name.contains("1لتر")) "لتر" else "كجم", 8.0, 7, i + 1) }

        // 8. المجمدات والبطاطس (8 أصناف)
        val frozenItems = listOf(
            "بطاطس فارم فريتس AAA تحمير كرتونة 10كجم", "خضار مشكل بسلة وجزر مجمد 1كجم", "ملوخية خضراء مفرومة زيرو مجمدة",
            "بامية زيرو خضراء إكسترا مجمدة", "ذرة حلوة صفراء حبوب مجمدة", "فاصوليا خضراء مقطعة مجمدة",
            "عجينة سبرينج رول مجمدة جاهزة", "ورق جلاش فاخر مجمد للفرن"
        )
        frozenItems.forEachIndexed { i, name -> add("cat-frozen-oils", name, if (name.contains("كرتونة")) "كرتونة" else "كيس", 10.0, 8, i + 1) }

        // 9. المخبوزات (صنفان أساسيان)
        add("cat-bread", "عيش بلدي فلاحي طازج يومي طري", "رغيف", 100.0, 9, 1)
        add("cat-bread", "خبز بريوش برجر سوبريم بالسمسم", "حبة", 80.0, 9, 2)

        // 10. التغليف والعلب (26 صنف)
        val packItems = listOf(
            "أطباق فويل ألومنيوم مستطيل 750 مل", "أغطية كرتون حراري لأطباق 750 مل", "أطباق فويل ألومنيوم 500 مل سرفيس",
            "أغطية كرتون لأطباق 500 مل", "أطباق فويل ميني 250 مل للشوربة والمقبلات", "أغطية كرتون لأطباق 250 مل",
            "علب ساندوتش برجر كرتون كرافت 450", "علب كريب ومثلثات كرتون كرافت 350", "علب شاورما عربي مقسمة صوصات",
            "أكياس ورقية كرافت تيك أواي يد مسطحة", "أكياس بلاستيك بيضاء ثقيلة مطبوعة لوجو", "بكر استرتش غذائي كلينج فيلم 45سم 1500م",
            "فويل ألومنيوم ثقيل مطابخ 40سم 200م", "ورق زبدة خبز فرنسي سيليكون 40x60سم", "أكواب ورقية دبل وول للقهوة 8 أونس",
            "أغطية أكواب ورقية بلاك سمارت", "أكواب عصير بلاستيك شفاة 16 أونس مع غطاء", "شفاطات عصير ورقية بيئية مقواة",
            "ملاعق بلاستيك ثقيلة معقمة كيس مفرد", "شوك بلاستيك سوداء ثقيلة معقمة", "سكاكين بلاستيك ثقيلة للمشويات",
            "مناديل سفرة مطبوعة لوجو علب 500 منديل", "رول مناديل مطبخ سحب تركي كبير 1كجم", "أكياس صوصات شفافة بلاستيك 2 أونس مع غطاء",
            "أكياس صوصات شفافة 4 أونس مع غطاء", "أكياس حرارية معزولة لحفظ الحرارة"
        )
        packItems.forEachIndexed { i, name -> add("cat-packaging", name, if (name.contains("بكر") || name.contains("رول") || name.contains("فويل")) "رول" else if (name.contains("أكياس") || name.contains("علب") || name.contains("أطباق")) "كرتونة" else "دستة", 5.0, 10, i + 1) }

        // 11. النظافة والمطهرات (17 صنف)
        val cleanItems = listOf(
            "صابون سائل جلي صحون تركيز عالي برميل 100 لتر", "كلور مركز تبييض وتعقيم أسطح 5 لتر", "صابون غسيل يد سائل مضاد للبكتيريا 5 لتر",
            "مسحوق تنظيف أرضيات معطر لافندر 10 لتر", "سلك مواعين استانلس ستيل ثقيل للمطابخ", "إسفنج تنظيف صحون وجهين خشن وناعم",
            "أكياس قمامة سوداء ثقيلة سعة 70 لتر كرتونة", "أكياس قمامة بيضاء سعة 100 لتر للغرف", "مطهر أيدي كحولي 70% بخاخ 1 لتر",
            "قفازات فحص طبية لاتكس بدون بودرة مقاس L", "قفازات فحص طبية نايتريل أزرق مقاس M", "أقنعة وجه طبية كمامات 3 طبقات",
            "شبيكة شعر بلاستيك مطاط للشيفات والمساعدين", "ممسحة أرضيات مايكروفايبر مع عصا حديد", "جردل ممسحة دوار استانلس ستيل",
            "أقراص كلور أقراص تعقيم خضار وسلطات", "بخاخات فارغة رشاش لمعقمات الأسطح"
        )
        cleanItems.forEachIndexed { i, name -> add("cat-cleaning", name, if (name.contains("لتر") || name.contains("برميل")) "جالون" else if (name.contains("علبة") || name.contains("كرتونة")) "علبة" else "قطعة", 4.0, 11, i + 1) }

        // 12. الكاشير والأدوات المكتبية (22 صنف)
        val cashierItems = listOf(
            "بكر كاشير حراري 80x80مم كرتونة 50 بكرة", "بكر ماكينات دفع فيزا حراري 57x40مم كرتونة", "أقلام جاف زرقاء ممتازة علبة 50 قلم",
            "أقلام ماركر فلومستر كتابة على الأكياس أحمر وأسود", "دفاتر بونات بوفيه مرقمة 3 أصل", "دباسة ورق ثقيلة معدن مقاس 24/6",
            "دبابيس دباسة مقاس 24/6 علب", "شريط لاصق سوليتيب شفاف عريض 5سم", "مسدس فواتير تسعير ولصق تواريخ",
            "بكر ملصقات تاريخ الإنتاج والصلاحية لمسدس التسعير", "آلة حاسبة مكتبية شاشة عريضة 12 خانة", "لوح كتابة كليب بورد استانلس لحفظ الشيتات",
            "كشاكيل تسجيل استلام يومي مقوى 200 صفحة", "ختم تاريخ استلام فوري حبر أحمر", "حبارة بديلة للأختام زرقاء وحمراء",
            "مقصات مطبخ حادة استانلس مقاومة للصدأ", "أكياس بلاستيك تيك أواي وسط يد مقواة", "أكياس بلاستيك تيك أواي مقاس كبير للطلبات الكبيرة",
            "منظم فواتير ومستندات أكورديون 12 جيب", "مشبك فواتير مطبخ حديدي تعليق أوردرات بار 60سم", "ميزان كاشير إلكتروني شاشة مزدوجة",
            "بطاريات قلم AA لماكينات الفحص وأجهزة الحرارة"
        )
        cashierItems.forEachIndexed { i, name -> add("cat-cashier", name, if (name.contains("كرتونة") || name.contains("علبة")) "علبة" else "قطعة", 3.0, 12, i + 1) }

        return list
    }

    fun getInitialSettings(): SystemSettingsEntity = SystemSettingsEntity(
        key = "default",
        purchasingPhone = "+201099887766",
        headChefPhone = "+201000000000",
        supplierPhonesCsv = "cat-veg:+201011122233,cat-meat:+201022233344,cat-poultry:+201055566677,cat-spices:+201044455566,cat-sauces:+201044455566,cat-grocery:+201088899900,cat-dairy:+201077788899,cat-frozen-oils:+201088899900,cat-bread:+201033344455,cat-packaging:+201099900011,cat-cleaning:+201055544433,cat-cashier:+201088877766"
    )
}
