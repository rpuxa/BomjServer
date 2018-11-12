package ru.rpuxa.bomjserver.server

import ru.rpuxa.bomjserver.CachedAction
import ru.rpuxa.bomjserver.CachedChainElement
import ru.rpuxa.bomjserver.CachedCourse
import java.lang.Exception
import java.util.*
import kotlin.properties.Delegates

const val RUB = 0
const val EURO = 1
const val BITCOIN = 2
const val BOTTLES = 3

const val ENERGY = 0
const val FOOD = 1
const val HEALTH = 2
const val JOBS = 3

object Actions {
    val actions: Array<CachedAction>
    val locations: Array<CachedChainElement>
    val friends: Array<CachedChainElement>
    val transports: Array<CachedChainElement>
    val homes: Array<CachedChainElement>
    val courses: Array<CachedCourse>

    private val actionsList = ArrayList<CachedAction>()


    val hash get() = Objects.hash(actions, locations, friends, transports, homes, courses)

    fun getActions(level: Int, menu: Int) =
            actions.filter { it.level.toInt() == level && it.menu.toInt() == menu }

    private inline val Int.rub get() = Actions.Money(this, RUB)
    private inline val Int.euro get() = Actions.Money(this, EURO)
    private inline val Int.bitcoin get() = Actions.Money(this, BITCOIN)
    private inline val Int.bottle get() = Actions.Money(this, BOTTLES)
    private val FREE = Actions.Money(0, RUB)

    init {
        courses = arrayListOf(
                Course(0, "Езда на самокате", ru.rpuxa.bomjserver.server.Actions.FREE, 10),
                Course(1, "Езда на велосипеде", 200.rub, 30),
                Course(2, "ПДД", 1000.rub, 100),
                Course(3, "Строение авто", 100.euro, 200),
                Course(4, "Строительство", 100.euro, 200),
                Course(5, "Программирование", 300.euro, 200),
                Course(6, "Торговле валютами", 600.euro, 200),
                Course(7, "Управление персоналом", 40.bitcoin, 200)
        ).map { CachedCourse(it.id.toByte(), it.name, it.money.value, it.money.currency.toByte(), it.length.toShort()) }.toTypedArray()

        locations = arrayOf(
                e("Помойка на окраине", 0, 0, 0, 0, 0, FREE),
                e("Подъезд", 1, 1, 0, 0, -1, 10.rub),
                e("Жилой район", 2, 2, 1, 1, -1, FREE),
                e("Дача", 3, 3, 3, 0, -1, FREE),
                e("Квартира в микрорайоне", 4, 4, 4, 0, -1, FREE),
                e("Центр города", 5, 5, 5, 0, -1, FREE),
                e("Берег моря", 6, 6, 5, 0, -1, FREE)
        )

        friends = arrayOf(
                e("Без кореша", 0, 0, 0, 0, 0, FREE),
                e("Сосед по подъезду Василий", 0, 0, 0, 1, -1, 60.bottle),
                e("Гопник Валера", 2, 0, 0, 1, -1, 300.bottle),
                e("Бомбила Семён", 3, 0, 0, 2, -1, 1000.rub),
                e("Прораб Михалыч", 4, 0, 0, 2, 4, 50.euro),
                e("Программист Слава", 5, 4, 0, 4, 5, 200.euro),
                e("Трейдер Юля", 6, 5, 0, 5, 6, 500.euro),
                e("Директор IT компании Эдвард", 6, 5, 0, 5, 7, 30.bitcoin)
        )

        transports = arrayOf(
                e("Без транспорта", 0, 0, 0, 0, -1, FREE),
                e("Самокат", 0, 0, 0, 0, 0, 500.rub),
                e("Велосипед", 0, 1, 0, 1, 1, 3000.rub),
                e("Старая копейка", 0, 1, 0, 1, 2, 10000.rub),
                e("Девятка", 0, 2, 3, 2, 3, 50000.rub),
                e("Старая Иномарка", 0, 2, 3, 2, 4, 3300.euro),
                e("Иномарка среднего класса", 0, 2, 3, 2, -1, 8000.euro),
                e("Спорткар", 0, 0, 0, 0, -1, 500.bitcoin)
        )

        homes = arrayOf(
                e("Помойка", 0, 0, 0, 0, 0, FREE),
                e("Палатка б/у", 0, 0, 0, 0, -1, 1000.rub),
                e("Гараж улитка", 2, 1, 0, 1, -1, 9000.rub),
                e("Сарай", 2, 1, 0, 1, -1, 40000.rub),
                e("Однушка", 2, 1, 0, 1, 0, 4000.euro),
                e("Двухкомнатная в центре города", 2, 1, 0, 1, 0, 9000.euro),
                e("Дом на берегу моря", 7, 1, 0, 1, 0, 600.bitcoin)
        )

        job(1) {
            add(3, "Пойти с Василием за бутылками", 45.bottle, -5, -20, -10)
            add(4, "Пособирать монеты из фонтана", 120.rub, -10, -20, -10)
            add(5, "Собирать макулатуру на свалках", 190.rub, -5, -40, -20)
            add(6, "Сдать люк на металл", 200.rub, -5, -20, -5, false)
        }

        job(2) {
            add(7, "Пособирать монеты из фонтана", 120.rub, -5, -20, -10)
            add(8, "Расклеивать листовки с Валерой", 250.rub, -10, -30, -20)
            add(9, "Стырить магнитолу", 300.rub, -10, -35, -20, false)
            add(10, "Отжать мобилку", 350.rub, -10, -45, -15, false)
        }

        job(3) {
            add(11, "Мести дворы", 150.rub, -5, -10, -5)
            add(12, "Подработать грузчиком", 300.rub, -5, -25, -5)
            add(13, "Работать бомбилой", 470.rub, -10, -30, -15)
            add(14, "Украсть сумочку", 500.rub, -10, -10, -10, false)
        }

        job(4) {
            add(15, "Месить цемент", 300.rub, -5, -10, -5)
            add(16, "Клеить обои", 550.rub, -5, -25, -5)
            add(17, "Класть плитку", 650.rub, -5, -30, -10)
            add(18, "Тырить вещи со стройки", 10.euro, -10, -10, -10, false)
        }

        job(5) {
            add(19, "Работать в колл-центре", 600.rub, -5, -10, -5)
            add(20, "Работать в службе поддержки", 10.euro, -5, -15, -5)
            add(21, "Кодить сайты", 15.euro, -10, -20, -15)
            add(22, "Написать вирус", 2.bitcoin, -15, -30, -30, false)
            add(23, "Написать Симулятор бомжа", 60.euro, -15, -30, -30, false)
        }

        job(6) {
            add(24, "Зарабатывать на бинарных опционах", 1000.rub, -5, -10, -5)
            add(25, "Управление капиталом", 17.euro, -5, -15, -5)
            add(26, "Продажа акций", 35.euro, -10, -200, -15)
            add(27, "Махинации с курсами валют", 3.bitcoin, -15, -30, -30, false)
        }

        job(7) {
            add(28, "Руководить в отделе безопасности", 25.euro, -5, -10, -5)
            add(29, "Управление компанией", 30.euro, -5, -15, -5)
            add(30, "Разработка ПО для иностранных коллег", 70.euro, -10, -20, -15)
            add(31, "Взлом базы данных конкурентов", 10.bitcoin, -15, -30, -30, false)
        }




        location(0) {
            food {
                rate = 124
                add(32, "Жрать объедки с помойки", free = true)
                add(33, "Купить бутер")
                add(34, "Купить шаурму")
                addIllegal(35, "Отжать семки у голубей")
            }

            health {
                rate = 124
                add(36, "Пособирать травы", free = true)
                add(108, "Купить настойку бояры")
                add(37, "Сходить к бабке")
                addIllegal(38, "Спереть лекарства с аптеки")
            }

            energy {
                rate = 90
                add(39, "Поспать", free = true)
                add(40, "Выпить палёнки")
                add(41, "Купить пивас")
                addIllegal(42, "Украсть Редбулл")
            }

            job {
                add(0, "Пособирать бутылки", 25.bottle, -10, -20, -10)
                add(1, "Пособирать монеты", 60.rub, -10, -20, -10)
                add(2, "Украсть бабки у уличных музыкантов", 100.rub, -10, -10, -10, false)
            }
        }

        location(1) {
            food {
                add(43, "Жрать объедки с помойки", FREE, -10, -15, 25)
                add(44, "Купить шаурму", 100.rub, -5, -5, 30)
                add(45, "Купить шашлык", 250.rub, -5, -5, 80)
                add(46, "Отнять еду у доставщика пиццы", FREE, -5, -5, 70, false)
            }
            health {
                add(47, "Пособирать травы", FREE, 10, -15, -5)
                add(48, "Купить пилюли", 50.rub, 35, -5, -5)
                add(49, "Найти в подъезде бывшего врача", 190.rub, 70, -10, -5)
                add(50, "Украсть у деда лекарства", FREE, 60, -15, -5, false)
            }
            energy {
                add(51, "Поспать в палатке", FREE, -5, 15, -5)
                add(52, "Бухнуть с гопниками", 50.rub, -5, 35, -5)
                add(53, "Купить палёный абсент", 150.rub, -15, 70, -5)
                add(54, "Украсть Редбулл", FREE, -10, 50, -5, false)
            }
        }

        location(2) {
            food {
                add(55, "Сварить гречку", 80.rub, -5, -5, 25)
                add(56, "Сделать похлебку", 270.rub, -5, -5, 50)
                add(57, "Купить птицу гриль", 450.rub, -5, -5, 80)
                add(58, "Отнять еду у доставщика пиццы", FREE, -5, -5, 70, false)
            }
            health {
                add(59, "Делать физ. упражнения", FREE, 10, -15, -5)
                add(60, "Купить пилюли", 100.rub, 30, -5, -5)
                add(61, "Приготовить лечебный отвар", 250.rub, 70, -10, -5)
                add(62, "Ограбить аптеку", FREE, 80, -20, -5, false)
            }
            energy {
                add(63, "Поспать в гараже", FREE, -5, 15, -5)
                add(64, "Найти собутыльников за гаражами", 100.rub, -5, 35, -5)
                add(65, "Купить коньяк", 280.rub, -5, 70, -5)
                add(66, "Отжать бухло у собутыльников", FREE, -10, 80, -5, false)
            }
        }

        location(3) {
            food {
                add(67, "Сварить мясной бульон", 170.rub, -5, -5, 25)
                add(68, "Пойти в магаз", 350.rub, -5, -5, 50)
                add(69, "Заказать пиццу", 600.rub, -5, -5, 80)
                add(70, "Ограбить ларек", FREE, -5, -5, 70, false)
            }
            health {
                add(71, "Купить лекарства", 180.rub, 30, -5, -5)
                add(72, "Сходить в больницу", 470.rub, 80, -10, -5)
                add(73, "Знакомый врач", 7.euro, 100, -10, -5)
            }
            energy {
                add(74, "Поспать", FREE, -5, 10, -5)
                add(75, "Купить водяры", 150.rub, -5, 35, -5)
                add(76, "Купить коньяк", 400.rub, -5, 50, -5)
                add(77, "Купить хорошего вина", 9.euro, -5, 100, -5)
            }
        }

        location(4) {
            food {
                add(78, "Пойти в Ашан", 350.rub, -5, -5, 30)
                add(79, "Заказать пиццу", 600.rub, -5, -5, 50)
                add(80, "Пойти в ресторан", 15.euro, -5, -5, 80)
            }
            health {
                add(81, "Гос больница", 500.rub, 40, -10, -5)
                add(82, "Знакомый врач", 7.euro, 40, -10, -5)
                add(83, "Частная больница", 15.euro, 80, -5, -5)
            }
            energy {
                add(84, "Поспать", FREE, -5, 10, -5)
                add(85, "Купить абсент", 400.rub, -5, 35, -5)
                add(86, "Купить коньяк", 10.euro, -5, 50, -5)
                add(87, "Купить дорогой виски", 15.euro, -5, 100, -5)
            }
        }

        location(5) {
            food {
                add(88, "Сходить в ТЦ", 600.rub, -5, -5, 30)
                add(89, "Сходить в шашлычную", 13.euro, -5, -5, 50)
                add(90, "Пойти в ресторан", 20.euro, -5, -5, 80)
            }
            health {
                add(91, "Вызвать врача", 800.rub, 25, -10, -5)
                add(92, "Купить иностранные таблетки", 12.euro, 40, -10, -5)
                add(93, "Частная клиника", 25.euro, 80, -5, -5)
            }
            energy {
                add(94, "Поспать", FREE, -5, 10, -5)
                add(95, "Сходить в спортзал", 700.rub, -5, 35, -5)
                add(96, "Сходить в фитнес клуб", 17.euro, -5, 50, -5)
                add(97, "Нанять тренера", 25.euro, -5, 100, -5)
            }
        }

        location(6) {
            food {
                add(98, "Сходить на рыбалку", 13.euro, -5, -5, 30)
                add(99, "Приготовить черепаху", 30.euro, -5, -5, 50)
                add(100, "Посетить ресторан на плаву", 40.euro, -5, -5, 80)
            }
            health {
                add(101, "Лечение пиявками", 15.euro, 20, -10, -5)
                add(102, "Частная клиника", 28.euro, 40, -10, -5)
                add(103, "Иностранные врачи", 50.euro, 80, -5, -5)
            }
            energy {
                add(104, "Поспать", FREE, -5, 10, -5)
                add(105, "Грязевые ванны", 15.euro, -5, 35, -5)
                add(106, "Сходить в фитнес клуб", 30.euro, -5, 50, -5)
                add(107, "Нырять с аквалангом", 50.euro, -5, 100, -5)
            }
        }
    }

    init {
        actions = actionsList.toTypedArray()
        for (actions)
    }


    private const val FREE_ACTION_ADD = 15
    private const val ILLEGAL_ACTION_ADD = 80
    private const val FREE_ACTION_REMOVE = -7
    private const val ACTION_REMOVE = -5

    private inline fun job(friend: Int, block: Job.() -> Unit) = Job(friend).block()

    private inline fun location(i: Int, block: Location.() -> Unit) = Location(i).block()

    private class Location(val i: Int) {
        inline fun energy(block: Menu.() -> Unit) = Menu(ENERGY).block()
        inline fun food(block: Menu.() -> Unit) = Menu(FOOD).block()
        inline fun health(block: Menu.() -> Unit) = Menu(HEALTH).block()

        internal inner class Menu(val type: Int) {
            //Сколько нужно потратить рублей за 100 ед
            var rate: Int by Delegates.notNull()

            private var count = 0

            fun add(id: Int, name: String, removeMoney: Money, health: Int, energy: Int, food: Int, legal: Boolean = true) {
                addAction(id, i, type, name, removeMoney, health, energy, food, legal)
            }

            fun add(id: Int, name: String, currency: Int = RUB, free: Boolean = false) {
                if (free) {
                    val (energy, food, health) = when (type) {
                        ENERGY -> Triple(FREE_ACTION_ADD, FREE_ACTION_REMOVE, FREE_ACTION_REMOVE)
                        FOOD -> Triple(FREE_ACTION_REMOVE, FREE_ACTION_ADD, FREE_ACTION_REMOVE)
                        HEALTH -> Triple(FREE_ACTION_REMOVE, FREE_ACTION_REMOVE, FREE_ACTION_ADD)
                        else -> throw Exception()
                    }
                    add(id, name, FREE, health, energy, food, true)
                    return
                }

                val actionAdd = when (count++) {
                    0 -> 30
                    1 -> 60
                    2 -> 90
                    3 -> 100
                    else -> throw Exception()
                }

                val cost = rate * actionAdd * getCost(RUB) / (100 * getCost(currency))

                val (energy, food, health) = when (type) {
                    ENERGY -> Triple(actionAdd, ACTION_REMOVE, ACTION_REMOVE)
                    FOOD -> Triple(ACTION_REMOVE, actionAdd, ACTION_REMOVE)
                    HEALTH -> Triple(ACTION_REMOVE, ACTION_REMOVE, actionAdd)
                    else -> throw Exception()
                }

                add(id, name, Money(cost, currency), health, energy, food, true)
            }

            fun addIllegal(id: Int, name: String) {
                val (energy, food, health) = when (type) {
                    ENERGY -> Triple(ILLEGAL_ACTION_ADD, FREE_ACTION_REMOVE, FREE_ACTION_REMOVE)
                    FOOD -> Triple(FREE_ACTION_REMOVE, ILLEGAL_ACTION_ADD, FREE_ACTION_REMOVE)
                    HEALTH -> Triple(FREE_ACTION_REMOVE, FREE_ACTION_REMOVE, ILLEGAL_ACTION_ADD)
                    else -> throw Exception()
                }
                add(id, name, FREE, health, energy, food, false)
            }
        }
    }

    private class Job(val i: Int) {
        fun add(id: Int, name: String, addMoney: Money, health: Int, energy: Int, food: Int, legal: Boolean = true) {
            addAction(id, i, JOBS, name, Money(-addMoney.value, addMoney.currency), health, energy, food, legal)
        }
    }

    private fun addAction(id: Int, level: Int, menu: Int, name: String, removeMoney: Money, health: Int, energy: Int, food: Int, legal: Boolean) {
        val action = CachedAction(
                id.toShort(), level.toByte(), menu.toByte(), name, removeMoney.value, removeMoney.currency.toByte(),
                energy.toShort(), food.toShort(), health.toShort(), !legal
        )
        actionsList.add(action)
    }

    private fun e(name: String, transport: Int, house: Int, friend: Int, location: Int, course: Int, moneyRemove: Money) =
            CachedChainElement(name, location.toByte(), friend.toByte(), transport.toByte(), house.toByte(), course.toByte(), moneyRemove.value, moneyRemove.currency.toByte())

    private class Course(
            val id: Int,
            val name: String,
            val money: Money,
            val length: Int
    )

    private class Money(val value: Int, val currency: Int)

    fun getCost(currency: Int) = when (currency) {
        RUB -> 2
        BOTTLES -> 3
        EURO -> 140
        BITCOIN -> 4000
        else -> throw Exception()
    }
}
