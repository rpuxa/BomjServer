package ru.rpuxa.bomjserver.server.actions

import java.util.*

const val RUB = 0
const val EURO = 1
const val BITCOIN = 2
const val BOTTLES = 3

const val ENERGY = 0
const val FOOD = 1
const val HEALTH = 2
const val JOBS = 3

object Actions {
    val actions = ArrayList<Action>()
    val locations = ArrayList<ChainElement>()
    val friends = ArrayList<ChainElement>()
    val transports = ArrayList<ChainElement>()
    val homes = ArrayList<ChainElement>()
    val courses: Array<Course>

    fun getActions(level: Int, menu: Int) =
            actions.filter { it.level == level && it.menu == menu }

    fun getActions(location: Int) =
            actions.filter { it.level == location }


    fun getCourse(id: Int) =
            courses.find { it.id == id }

    private val Int.rub get() = Money(this, RUB)
    private val Int.euro get() = Money(this, EURO)
    private val Int.bitcoin get() = Money(this, BITCOIN)
    private val Int.bottle get() = Money(this, BOTTLES)
    private val FREE = Money(0, RUB)

    init {
        courses = arrayOf(
                Course(0, "Езда на самокате", 50.rub, 10),
                Course(1, "Езда на велосипеде", 200.rub, 30),
                Course(2, "ПДД", 1000.rub, 50),
                Course(3, "Строение авто", 100.euro, 100),
                Course(4, "Строительство", 100.euro, 100),
                Course(5, "Программирование", 300.euro, 100),
                Course(6, "Торговле валютами", 600.euro, 100),
                Course(7, "Управление персоналом", 40.bitcoin, 100)
        )

        location(0, "Помойка на окраине") {
            friend("Без кореша", FREE)
            transport("Без транспорта", FREE)
            home("Без дома", FREE)

            food {
                add(32, "Жрать объедки с помойки", free = true)
                add(33, "Купить бутер")
                add(34, "Купить шаурму")
                add(35, "Отжать семки у голубей", illegal = true)
            }

            health {
                add(36, "Пособирать травы", free = true)
                add(108, "Купить настойку бояры")
                add(37, "Сходить к бабке")
                add(38, "Спереть лекарства с аптеки", illegal = true)
            }

            energy {
                add(39, "Поспать на помойке", free = true)
                add(40, "Выпить палёнки")
                add(41, "Купить пивас")
                add(42, "Украсть Редбулл", illegal = true)
            }

            job {
                add(0, "Пособирать бутылки", currency = BOTTLES)
                add(1, "Пособирать монеты")
                add(2, "Украсть бабки у уличных музыкантов", illegal = true)
            }
        }

        location(1, "Подъезд") {
            friend("Сосед по подъезду Василий", 60.bottle)
            transport("Самокат", 500.rub, 0)
            home("Палатка б/у", 1000.rub)

            food {
                add(43, "Жрать объедки с помойки", free = true)
                add(44, "Купить шаурму")
                add(45, "Купить шашлык")
                add(46, "Отнять еду у доставщика пиццы", illegal = true)
            }
            health {
                add(47, "Пособирать травы", free = true)
                add(48, "Купить пилюли")
                add(49, "Найти в подъезде бывшего врача")
                add(50, "Украсть у деда лекарства", illegal = true)
            }
            energy {
                add(51, "Поспать в палатке", free = true)
                add(52, "Бухнуть с гопниками")
                add(53, "Купить палёный абсент")
                add(54, "Украсть Редбулл", illegal = true)
            }

            job {
                add(3, "Пойти с Василием за бутылками", currency = BOTTLES)
                add(4, "Пособирать монеты из фонтана")
                add(5, "Собирать макулатуру на свалках")
                add(6, "Сдать люк на металл", illegal = true)
            }
        }

        location(2, "Гаражи") {
            friend("Гопник Валера", 300.bottle)
            transport("Велосипед", 3000.rub, 1)
            home("Гараж улитка", 9000.rub)

            food {
                add(55, "Сварить гречку")
                add(56, "Сделать похлебку")
                add(57, "Купить птицу гриль")
                add(58, "Отнять еду у доставщика пиццы", illegal = true)
            }
            health {
                add(59, "Делать физ. упражнения", free = true)
                add(60, "Купить пилюли")
                add(61, "Приготовить лечебный отвар")
                add(62, "Ограбить аптеку", illegal = true)
            }
            energy {
                add(63, "Поспать в гараже", free = true)
                add(64, "Найти собутыльников за гаражами")
                add(65, "Купить коньяк")
                add(66, "Отжать бухло у собутыльников", illegal = true)
            }
            job {
                add(7, "Пособирать монеты из фонтана")
                add(8, "Расклеивать листовки с Валерой")
                add(9, "Стырить магнитолу", illegal = true)
                add(10, "Отжать мобилку", illegal = true)
            }
        }

        location(3, "Заброшка") {
            friend("Бомбила Семён", 1000.rub)
            transport("Старая копейка", 10000.rub, 2)
            home("Отремонтированная комната в заброшке", 15700.rub)

            food {
                add(109, "Заварить доширак")
                add(110, "Пожарить котлет")
                add(110, "Сварить рис")
                add(111, "Ограбить ларек", illegal = true)
            }

            health {
                add(112, "Сходить в поликлинику")
            }

            energy {

            }

            job {
                add(11, "Мести дворы")
                add(12, "Подработать грузчиком")
                add(13, "Работать бомбилой")
                add(14, "Украсть сумочку", illegal = true)
            }
        }

        location(4, "Дача") {
            friend("Прораб Михалыч", 50.euro, 4)
            transport("Девятка", 50000.rub, 3)
            home("Сарай", 40000.rub)

            food {
                add(67, "Сварить мясной бульон")
                add(68, "Пойти в магаз")
                add(69, "Заказать пиццу")
                add(70, "Ограбить ларек", illegal = true)
            }

            health {
                add(71, "Купить лекарства")
                add(72, "Сходить в больницу")
                add(73, "Знакомый врач", currency = EURO)
            }

            energy {
                add(74, "Поспать", free = true)
                add(75, "Купить водяры")
                add(76, "Купить коньяк")
                add(77, "Купить хорошего вина", currency = EURO)
            }

            job {
                add(15, "Месить цемент")
                add(16, "Клеить обои")
                add(17, "Класть плитку")
                add(18, "Тырить вещи со стройки", currency = EURO, illegal = true)
            }
        }

        location(5, "Квартира в микрорайоне") {
            friend("Программист Слава", 200.euro, 5)
            transport("Старая Иномарка", 3300.euro)
            home("Однушка", 4000.euro)

            food {
                add(78, "Пойти в Ашан")
                add(79, "Заказать пиццу")
                add(80, "Пойти в ресторан", currency = EURO)
            }
            health {
                add(81, "Гос больница")
                add(82, "Знакомый врач", currency = EURO)
                add(83, "Частная больница", currency = EURO)
            }
            energy {
                add(84, "Поспать", free = true)
                add(85, "Купить абсент")
                add(86, "Купить коньяк", currency = EURO)
                add(87, "Купить дорогой виски", currency = EURO)
            }

            job {
                add(19, "Работать в колл-центре")
                add(20, "Работать в службе поддержки", currency = EURO)
                add(21, "Кодить сайты", currency = EURO)
                add(22, "Написать вирус", currency = EURO, illegal = true)
                add(23, "Написать Симулятор бомжа", currency = EURO, illegal = true)
            }
        }

        location(6, "Центр города") {
            friend("Трейдер Юля", 500.euro, 6)
            transport("Иномарка среднего класса", 8000.euro)
            home("Двухкомнатная в центре города", 9000.euro)

            food {
                add(88, "Сходить в ТЦ")
                add(89, "Сходить в шашлычную", currency = EURO)
                add(90, "Пойти в ресторан", currency = EURO)
            }
            health {
                add(91, "Вызвать врача")
                add(92, "Купить иностранные таблетки", currency = EURO)
                add(93, "Частная клиника", currency = EURO)
            }
            energy {
                add(94, "Поспать", free = true)
                add(95, "Сходить в спортзал")
                add(96, "Сходить в фитнес клуб", currency = EURO)
                add(97, "Нанять тренера", currency = EURO)
            }

            job {
                add(24, "Зарабатывать на бинарных опционах")
                add(25, "Управление капиталом", currency = EURO)
                add(26, "Продажа акций", currency = EURO)
                add(27, "Махинации с курсами валют", currency = BITCOIN)
            }
        }

        location(7, "Берег моря") {
            friend("Директор IT компании Эдвард", 30.bitcoin, 7)
            transport("Спорткар", 500.bitcoin)
            home("Дом на берегу моря", 600.bitcoin)

            food {
                add(98, "Сходить на рыбалку", currency = EURO)
                add(99, "Приготовить черепаху", currency = EURO)
                add(100, "Посетить ресторан на плаву", currency = EURO)
            }
            health {
                add(101, "Лечение пиявками", currency = EURO)
                add(102, "Частная клиника", currency = EURO)
                add(103, "Иностранные врачи", currency = EURO)
            }
            energy {
                add(104, "Поспать", free = true)
                add(105, "Грязевые ванны", currency = EURO)
                add(106, "Сходить в фитнес клуб", currency = EURO)
                add(107, "Нырять с аквалангом", currency = EURO)
            }

            job {
                add(28, "Руководить в отделе безопасности", currency = EURO)
                add(29, "Управление компанией", currency = EURO)
                add(30, "Разработка ПО для иностранных коллег", currency = EURO)
                add(31, "Взлом базы данных конкурентов", currency = BITCOIN)
            }
        }
    }

    private inline fun location(id: Int, name: String, course: Int = -1, block: Location.() -> Unit) {
        locations.add(ChainElement(name, course, FREE))
        Location(id).block()
    }

    private class Location(val level: Int) {

        inline fun energy(block: Menu.() -> Unit) = Menu(level, ENERGY).block()

        inline fun food(block: Menu.() -> Unit) = Menu(level, FOOD).block()

        inline fun health(block: Menu.() -> Unit) = Menu(level, HEALTH).block()

        inline fun job(block: Menu.() -> Unit) = Menu(level, JOBS).block()

        fun friend(name: String, cost: Money, course: Int = -1) {
            friends.add(ChainElement(name, course, cost))
        }

        fun home(name: String, cost: Money, course: Int = -1) {
            homes.add(ChainElement(name, course, cost))
        }

        fun transport(name: String, cost: Money, course: Int = -1) {
            transports.add(ChainElement(name, course, cost))
        }
    }

    private class Menu(val level: Int, val type: Int) {

        fun add(id: Int, name: String, currency: Int = RUB, free: Boolean = false, illegal: Boolean = false) {
            actions.add(Action(id, name, level, type, currency, free || illegal, illegal))
        }
    }
}

