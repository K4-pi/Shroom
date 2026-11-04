package com.example.shroom.data

import android.graphics.drawable.Drawable
import android.media.Image
import androidx.compose.ui.res.painterResource
import androidx.core.graphics.drawable.toDrawable
import com.example.shroom.R

enum class Edibility {
    Edible,
    Caution,
    Poisonous,
}

data class Mushroom(
    val id: String,
    val commonName: String,
    val latinName: String,
    val edibility: Edibility,
    val habitat: String,
    val season: String,
    val description: String,
    val alchemistNote: String,
    val lookalikes: String,
    val imageRes: Int,
)

object MushroomTome {
    val all: List<Mushroom> = listOf(
        Mushroom(
            id = "Boletus edulis",
            commonName = "Borowik szlachetny",
            latinName = "Boletus edulis",
            edibility = Edibility.Edible,
            habitat = "lasy iglaste, liściaste, jak i mieszane",
            season = "Maj – Listopad (rzadko Grudzień)",
            description = """
                Występuje w Ameryce Północnej (głównie na zachód od Gór Skalistych, rzadziej w części centralnej i wschodniej kontynentu) i w Europie[12]. 
                Ponadto został zawleczony do Nowej Zelandii[13] i południowej Afryki[14]. W Polsce często spotykany zwłaszcza w górach, rzadziej na niżu, 
                zwykle rzadki w okolicach wielkich miast[potrzebny przypis].
                Borowik szlachetny występuje zarówno w lasach iglastych, liściastych, jak i mieszanych. 
                Grzyb ten tworzy mikoryzę z licznymi gatunkami drzew, jednak głównie ze świerkami dlatego najobficiej występuje w 
                górskich drzewostanach świerkowych. Preferuje drzewostany w średniej klasie wieku, w starszych drzewostanach iglastych 
                ustępuje podgrzybowi brunatnemu[15][8]. Owocniki pojawiają się najczęściej i najliczniej w okresie od maja do listopada, 
                zdarzają się także w grudniu (np. w 2000 roku)[16]. Owocniki wyrastają pojedynczo, często w niewielkich grupach po kilka, bywa także, że tworzą kręgi[10.2].
                Do prawidłowego rozwoju borowika szlachetnego potrzebne są nie tylko korzenie właściwego gatunku drzewa. 
                Na powierzchni kory tworzy on ryzomorfy – wyraźnie widoczne gołym okiem sznury grzybniowe, które rozchodzą się w glebie 
                w różnych kierunkach i tworzą okazałe owocniki. Jednak aby strzępki jego grzybni połączyły się z korzeniami drzew, w podłożu 
                muszą się znajdować bakterie z rodzaju Pseudomonas.
            """.trimIndent(),
            alchemistNote = """
                 Borowik szlachetny wyróżnia się wśród grzybów wysoką zawartością białka wynoszącą ok. 5,5%. 
                 Mimo podobnej zawartości białek jak w chlebie i mleku – białka borowika są w części tylko dostępne ze względu na 
                 niemożność strawienia przez człowieka ścian komórkowych zbudowanych z chityny. Dostępność białek zwiększa rozdrabnianie grzybów i
                  ich obróbka cieplna. Dominującym składnikiem jest woda (80–90%). Pozostałe składniki to głównie węglowodany, przy czym dominuje 
                  wspomniana chityna. Nieznaczny, kilkuprocentowy udział mają strawne węglowodany. Około 1% stanowią składniki mineralne. 
                  W zestawieniach porównawczych wartości odżywczych popularnych produktów[23] borowik suszony zajmuje pierwsze miejsce pod 
                  względem zawartości fosforu, witaminy B2, witaminy B3, pod względem zawartości żelaza ustępuje tylko kaszance, jest też stosunkowo 
                  bogatym źródłem magnezu i witaminy C. Borowik szlachetny należy do grzybów wyróżniających się gromadzeniem niewielkich tylko ilości cezu. 
                  Obecność tego promieniotwórczego pierwiastka stwierdzono w zwiększonych ilościach w środkowoeuropejskich grzybach po katastrofie w Czarnobylu[10.3]. 
                  Z kolei na 20 gatunków grzybów zbieranych w Polsce, właśnie borowik szlachetny cechuje się najwyższą koncentracją promieniotwórczego izotopu polonu 210Po, 
                  dla którego gatunek ten jest doskonałym bioindykatorem. Dla przyjęcia dawki promieniowania w wysokości 37 μSv trzeba byłoby spożyć 5 kg świeżych i silnie 
                  skażonych grzybów
            """.trimIndent(),
            lookalikes = "Goryczak żółciowy",
            imageRes = R.drawable.borowik
        ),
        Mushroom(
            id = "Russula emetica",
            commonName = "Gołąbek wymiotny",
            latinName = "Russula emetica",
            edibility = Edibility.Poisonous,
            habitat = "Lasy iglaste, szczególnie w podgórskich lasach świerkowych i w nizinnych lasach sosnowych.",
            season = "Maj – Listopad (rzadko Grudzień)",
            description = """
                Owocnik gołąbka wymiotnego osiąga średnicę od 5 do 11 cm. U młodych osobników ma wypukły kształt, później robi się rozpostarty z wklęsłym środkiem i krótko żłobkowanym brzegiem. Może przybierać barwę cynową lub krwistoczerwoną.
                U starszych egzemplarzy kolor robi się wyblakły. Ma gładką, kleistą i połyskującą skórę, którą łatwo się zdejmuje.
                Blaszki gołąbka wymiotnego są cienkie, szeroko rozstawione, zaokrąglone przy trzonie i wolne. Wszystkie są jednakowej długości. Mają biały kolor.
                Trzon gołąbka wymiotnego osiąga wysokość od 4 do 8 cm. i grubość od 1 do 2 cm. Ma biały kolor, chociaż zdarzają się osobniki z czerwonymi przebarwieniami. 
                U młodych okazów ma cylindryczny kształt i jest pełny. U starszych osobników robi się walcowaty i pusty. Łatwo się łamie.
            """.trimIndent(),
            alchemistNote = """
                 Gołąbek wymiotny jest grzybem trującym. Zatrucie powodują substancje żywicowe 
                 i terpenowe, które działają drażniąco na błonę śluzową przewodu pokarmowego,  powodując objawy ostrego nieżytu żołądkowo–jelitowego. 
            """.trimIndent(),
            lookalikes = """gołąbek błotny, gołąbek buczynowy, gołąbek czerwononogi, gołąbek krwisty""".trimIndent(),
            imageRes = R.drawable.golabek_wymiotny
        ),
//        Mushroom(
//            id = "porcini",
//            commonName = "Porcini",
//            latinName = "Boletus edulis",
//            edibility = Edibility.Edible,
//            habitat = "Under spruce, pine, beech, and oak",
//            season = "Late summer – Autumn",
//            description = "Stout brown-capped bolete with a white to olive sponge underside. Flesh does not blue when cut.",
//            alchemistNote = "The merchant's favorite. Thick flesh yields rich decoctions of vigor and appetite.",
//            lookalikes = "Bitter bolete (Tylopilus felleus) — pink pores, bitter taste. Never swallow a trial nibble of unknown boletes.",
//        ),
//        Mushroom(
//            id = "morel",
//            commonName = "Morel",
//            latinName = "Morchella esculenta",
//            edibility = Edibility.Edible,
//            habitat = "Disturbed ground, orchards, burn sites, riverbanks",
//            season = "Spring",
//            description = "Honeycomb-pitted conical cap fused to a hollow stem. Must be cooked thoroughly.",
//            alchemistNote = "Spring's first gold. Hollow chambers once held whispers of forest fire and renewal.",
//            lookalikes = "False morels (Gyromitra) — brain-like, wrinkled, not pitted. Toxic if mishandled.",
//        ),
//        Mushroom(
//            id = "oyster",
//            commonName = "Oyster Mushroom",
//            latinName = "Pleurotus ostreatus",
//            edibility = Edibility.Edible,
//            habitat = "Dead hardwood trunks and stumps",
//            season = "Autumn – Winter",
//            description = "Shelf-like clusters with soft gills running down a short eccentric stem. Mild anise scent when fresh.",
//            alchemistNote = "Reliable shelf fungus. Good for beginner tonics and thickening broths of stamina.",
//            lookalikes = "Some toxic shelf fungi; check for white spore print and soft flesh.",
//        ),
//        Mushroom(
//            id = "shiitake",
//            commonName = "Shiitake",
//            latinName = "Lentinula edodes",
//            edibility = Edibility.Edible,
//            habitat = "Cultivated on hardwood logs; wild in East Asia",
//            season = "Year-round (cultivated)",
//            description = "Umbrella brown caps with white gills and a fibrous stem. Famous savory (umami) character.",
//            alchemistNote = "A cultured ingredient of trade caravans. Deepens restorative elixirs.",
//            lookalikes = "Rarely confused when cultivated; wild lookalikes vary by region.",
//        ),
//        Mushroom(
//            id = "fly_agaric",
//            commonName = "Fly Agaric",
//            latinName = "Amanita muscaria",
//            edibility = Edibility.Poisonous,
//            habitat = "Birch and conifer woods",
//            season = "Late summer – Autumn",
//            description = "Iconic scarlet (or orange) cap freckled with white warts, white gills, and a ringed stem with a bulbous base.",
//            alchemistNote = "The tome's warning seal. Beautiful, legendary — and unsafe. Leave it for the manuscript illuminators.",
//            lookalikes = "Other Amanitas. Cap color fades with rain; never rely on spots alone.",
//        ),
//        Mushroom(
//            id = "death_cap",
//            commonName = "Death Cap",
//            latinName = "Amanita phalloides",
//            edibility = Edibility.Poisonous,
//            habitat = "Under oak and other hardwoods",
//            season = "Summer – Autumn",
//            description = "Greenish to yellowish cap, white gills, ring, and a cup-like volva at the base. Deadly toxic.",
//            alchemistNote = "The black page of every alchemist's book. No antidote in the shop. Identify, then walk away.",
//            lookalikes = "Often mistaken for edible puffballs (when young) or greenish russulas. Dig the base — seek the volva.",
//        ),
//        Mushroom(
//            id = "destroying_angel",
//            commonName = "Destroying Angel",
//            latinName = "Amanita virosa / bisporigera",
//            edibility = Edibility.Poisonous,
//            habitat = "Woodland soils, often with hardwoods",
//            season = "Summer – Autumn",
//            description = "Pure white amanita with a skirted ring and volva. All parts deadly.",
//            alchemistNote = "White as vellum, quiet as night. Marked with a wax seal of absolute prohibition.",
//            lookalikes = "Young edible Agaricus; always check for volva and white gills that do not pinken.",
//        ),
//        Mushroom(
//            id = "liberty_cap",
//            commonName = "Liberty Cap",
//            latinName = "Psilocybe semilanceata",
//            edibility = Edibility.Caution,
//            habitat = "Grassy pastures, often with livestock",
//            season = "Autumn",
//            description = "Small conical sticky caps with a pointed nipple; thin stems. Psychoactive — illegal to possess in many lands.",
//            alchemistNote = "Not for the shop shelves. Laws of the realm apply; the tome records it for recognition only.",
//            lookalikes = "Many small brown grassland mushrooms (LBMs). Never guess.",
//        ),
//        Mushroom(
//            id = "honey_fungus",
//            commonName = "Honey Fungus",
//            latinName = "Armillaria mellea",
//            edibility = Edibility.Caution,
//            habitat = "Living and dead trees; forms black bootlace rhizomorphs",
//            season = "Autumn",
//            description = "Honey-colored clustered caps with a ring. Must be well cooked; disagreeable to some.",
//            alchemistNote = "The forest's slow conqueror. Usable in hardy stews after thorough boiling — if your stomach agrees.",
//            lookalikes = "Galera and other small brown mushrooms; confirm ring and clustered habit.",
//        ),
//        Mushroom(
//            id = "puffball",
//            commonName = "Giant Puffball",
//            latinName = "Calvatia gigantea",
//            edibility = Edibility.Edible,
//            habitat = "Meadows, pastures, woodland edges",
//            season = "Late summer – Autumn",
//            description = "Football-sized white sphere. Edible only when flesh is pure white throughout.",
//            alchemistNote = "Slice like bread for pan-seared plates. Any yellow or purple within — discard.",
//            lookalikes = "Immature amanitas (egg stage). Always section vertically to rule out a hidden stem and gills.",
//        ),
//        Mushroom(
//            id = "hedgehog",
//            commonName = "Hedgehog Mushroom",
//            latinName = "Hydnum repandum",
//            edibility = Edibility.Edible,
//            habitat = "Mixed woods, often under conifers",
//            season = "Summer – Autumn",
//            description = "Pale orange to cream caps with spines (teeth) under the cap instead of gills.",
//            alchemistNote = "Gentle spines, gentle flavour. A forgiving find for careful novices.",
//            lookalikes = "Other tooth fungi; generally safer group when spines are clear.",
//        ),
//        Mushroom(
//            id = "ink_cap",
//            commonName = "Shaggy Ink Cap",
//            latinName = "Coprinus comatus",
//            edibility = Edibility.Caution,
//            habitat = "Lawns, roadsides, disturbed soil",
//            season = "Spring – Autumn",
//            description = "Tall shaggy white caps that dissolve into black ink as they mature. Eat young; avoid alcohol nearby.",
//            alchemistNote = "Writes its own end. Collect before deliquescence. Some kin react poorly with wine.",
//            lookalikes = "Other ink caps; common ink cap (Coprinopsis atramentaria) is famously alcohol-intolerant.",
//        ),
//        Mushroom(
//            id = "russula_emetica",
//            commonName = "The Sickener",
//            latinName = "Russula emetica",
//            edibility = Edibility.Poisonous,
//            habitat = "Mossy coniferous woods",
//            season = "Summer – Autumn",
//            description = "Bright red brittle cap, white gills and stem. Causes gastrointestinal distress.",
//            alchemistNote = "Pretty as a wax seal, foul as a spoiled brew. Brittle flesh snaps clean — still leave it be.",
//            lookalikes = "Other red russulas; some are edible, many are not. Taste-spit tests are for experts only.",
//        ),
    )

    fun byId(id: String): Mushroom? = all.find { it.id == id }

    fun search(query: String): List<Mushroom> {
        if (query.isBlank()) return all
        val q = query.trim().lowercase()
        return all.filter {
            it.commonName.lowercase().contains(q) ||
                it.latinName.lowercase().contains(q) ||
                it.habitat.lowercase().contains(q) ||
                it.description.lowercase().contains(q)
        }
    }
}
