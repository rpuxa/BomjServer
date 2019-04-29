package ru.rpuxa.bomjserver.server.actions

import java.util.*

const val RUB = 0
const val BOTTLES = 1

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
    private val Int.bottle get() = Money(this, BOTTLES)
    private val FREE = Money(0, RUB)

    init {
        courses = arrayOf(
                Course(0, "Езда на самокате", 50.rub, 10),
                Course(1, "Езда на велосипеде", 200.rub, 30),
                Course(2, "ПДД", 900.rub, 50),
                Course(3, "Строение авто", (90 * 70).rub, 100),
                Course(4, "Строительство", (90 * 70).rub, 100),
                Course(5, "Программирование", (290 * 70).rub, 100),
                Course(6, "Торговле валютами", (490 * 70).rub, 100),
                Course(7, "Управление персоналом", (39 * 2000).rub, 100)
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
                add(121, "Драться за бутылки", currency = BOTTLES)
                add(1, "Пособирать монеты")
                add(2, "Украсть бабки у уличных музыкантов", illegal = true)
            }
        }

        location(1, "Подъезд") {
            friend("Сосед по подъезду Василий", 60.bottle)
            transport("Самокат", 400.rub, 0)
            home("Палатка б/у", 700.rub)

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
            transport("Велосипед", 1900.rub, 1)
            home("Гараж улитка", 7800.rub)

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
            friend("Бомбила Семён", 500.rub)
            transport("Старая копейка", 9000.rub, 2)
            home("Отремонтированная комната в заброшке", 9000.rub)

            food {
                add(109, "Заварить доширак")
                add(110, "Сварить рис")
                add(111, "Пожарить котлет")
                add(112, "Ограбить ларек", illegal = true)
            }

            health {
                add(113, "Помолиться", free = true)
                add(114, "Купить витамины")
                add(115, "Сходить в поликлинику")
                add(116, "Прессануть фармацевта", illegal = true)
            }

            energy {
                add(117, "Поспать", free = true)
                add(118, "Купить вискарь")
                add(119, "Купить редбулл")
                add(120, "Сходить в кино")
            }

            job {
                add(11, "Мести дворы")
                add(12, "Подработать грузчиком")
                add(13, "Работать бомбилой")
                add(14, "Украсть сумочку", illegal = true)
            }
        }

        location(4, "Дача") {
            friend("Прораб Михалыч", (29 * 70).rub, 4)
            transport("Девятка", 29000.rub, 3)
            home("Сарай", 27900.rub)

            food {
                add(67, "Сварить мясной бульон")
                add(69, "Заказать пиццу")
                add(68, "Пойти в магаз")
                add(70, "Ограбить ларек", illegal = true)
            }

            health {
                add(71, "Купить антибиотики")
                add(72, "Сходить в больницу")
                add(73, "Знакомый врач")
            }

            energy {
                add(74, "Поспать", free = true)
                add(75, "Купить водяры")
                add(76, "Купить коньяк")
                add(77, "Купить хорошего вина")
            }

            job {
                add(15, "Месить цемент")
                add(16, "Клеить обои")
                add(17, "Класть плитку")
                add(18, "Тырить вещи со стройки", illegal = true)
            }
        }

        location(5, "Квартира в микрорайоне") {
            friend("Программист Слава", (90 * 70).rub, 5)
            transport("Старая Иномарка", (1900 * 70).rub)
            home("Однушка", (2200 * 70).rub)

            food {
                add(78, "Пойти в Ашан")
                add(79, "Заказать пиццу")
                add(80, "Пойти в ресторан")
            }
            health {
                add(81, "Гос больница")
                add(82, "Знакомый врач")
                add(83, "Частная больница")
            }
            energy {
                add(84, "Поспать", free = true)
                add(85, "Купить абсент")
                add(86, "Купить коньяк")
                add(87, "Купить дорогой виски")
            }

            job {
                add(19, "Работать в колл-центре")
                add(20, "Работать в службе поддержки")
                add(21, "Кодить сайты")
                add(22, "Написать вирус", illegal = true)
                add(23, "Написать Симулятор бомжа", illegal = true)
            }
        }

        location(6, "Центр города") {
            friend("Трейдер Юля", (290 * 70).rub, 6)
            transport("Иномарка среднего класса", (3900 * 70).rub)
            home("Двухкомнатная квартира", (3990 * 70).rub)

            food {
                add(88, "Сходить в ТЦ")
                add(89, "Сходить в шашлычную")
                add(90, "Пойти в ресторан")
            }
            health {
                add(91, "Вызвать врача")
                add(92, "Купить иностранные таблетки")
                add(93, "Частная клиника")
            }
            energy {
                add(94, "Поспать", free = true)
                add(95, "Сходить в спортзал")
                add(96, "Сходить в фитнес клуб")
                add(97, "Нанять тренера")
            }

            job {
                add(24, "Зарабатывать на бинарных опционах")
                add(25, "Управление капиталом")
                add(26, "Продажа акций")
                add(27, "Махинации с курсами валют", illegal = true)
            }
        }

        location(7, "Заграница") {
            friend("Директор IT компании Эдвард", (19 * 2000).rub, 7)
            transport("Внедорожник", (340 * 2000).rub)
            home("Частный дом", (450 * 2000).rub)

            food {
                add(98, "Сходить на рыбалку")
                add(99, "Приготовить черепаху")
                add(100, "Посетить ресторан на плаву")
            }
            health {
                add(101, "Лечение пиявками")
                add(102, "Частная клиника")
                add(103, "Иностранные врачи")
            }
            energy {
                add(104, "Поспать", free = true)
                add(105, "Грязевые ванны")
                add(106, "Сходить в фитнес клуб")
                add(107, "Нырять с аквалангом")
            }

            job {
                add(28, "Руководить в отделе безопасности")
                add(29, "Управление компанией")
                add(30, "Разработка ПО для иностранных коллег")
                add(31, "Взлом базы данных конкурентов", illegal = true)
            }
        }
    }

    init {
        for (id in 0..actions.maxBy { it.id }!!.id) {
            val count = actions.count { it.id == id }
            if (count != 1)
                throw IllegalStateException("Wrong id exception. Count = $count. Id = $id")
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

