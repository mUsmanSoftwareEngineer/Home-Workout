package fitnessapp.workout.homeworkout.stretching.objects



open class SideMenuItem {

    lateinit var ID:String
    var icon:Int
    lateinit var name:String
    var selected:Boolean = false

    constructor(ID: String, icon: Int, name: String)
    {
        this.icon = icon
        this.ID = ID
        this.name = name
    }
    constructor(icon: Int, name: String)
    {
        this.icon = icon
        this.name = name
    }

}