package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {
    val driverInList = trips.map { it.driver }
    return allDrivers.filter { it -> it !in driverInList }.toSet()
}

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    if (minTrips == 0) {
        return this.allPassengers
    }
    val listOfPassengersWithTrips = this.trips
            .flatMap { it.passengers }
            .groupingBy { it }
            .eachCount()
    return listOfPassengersWithTrips
            .filter { it.value >= minTrips }
            .map { it.key }
            .toSet()
}

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
    val passengers = this.trips
            .filter { it.driver == driver }
            .flatMap { it.passengers }
            .groupingBy { it }
            .eachCount()
    return passengers
            .filter { it.value > 1 }
            .map { it.key }
            .toSet()
}

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val passengersDiscounted = this.trips
            .filter { it.discount != null && it.discount > 0.0 }
            .flatMap { it.passengers }
            .groupingBy { it }
            .eachCount()

    val passengersNoDiscounted = this.trips
            .filter { it.discount == null || it.discount == 0.0 }
            .flatMap { it.passengers }
            .groupingBy { it }
            .eachCount()

    return passengersDiscounted
            .filter { it.value > (passengersNoDiscounted[it.key] ?: 0) }
            .map { it.key }
            .toSet()
}


/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val tripsDuration = this.trips
            .groupBy { it.duration / 10 }
            .map { Pair(it.key, it.value.count()) }
            .maxBy { p -> p.second }
            ?.first

    val min = tripsDuration?.times(10)
    val max = min?.plus(9) ?: 0
    return min?.rangeTo(max)
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {

    if (this.trips.isEmpty() || this.allDrivers.isEmpty() || this.allPassengers.isEmpty()) {
        return false
    }

    val drivers = this.allDrivers

    val driverIncome = this.trips
            .groupBy { t -> t.driver }
            .map { e -> Pair(e.key, e.value.sumByDouble { t -> t.cost }) }
            .sortedByDescending { (_, value) -> value }
            .toList()


    val twentyDrivers = (drivers.count() * 20) / 100

    val driversTheBest = driverIncome
            .take(twentyDrivers)
            .map { it.first }

    val resIncome: Double = this.trips
            .sumByDouble { it.cost }

    val eightyIncome = (resIncome * 80) / 100

    val incomeByTopDriver = this.trips
            .filter { it.driver in driversTheBest }
            .map { it.cost }
            .sum()

    return incomeByTopDriver >= eightyIncome
}