package me.archdev.restapi.core.profiles

import me.archdev.restapi.core.UserProfile
import me.archdev.restapi.utils.db.DatabaseConnector

private[profiles] trait UserProfileTable {

  protected val databaseConnector: DatabaseConnector
  import databaseConnector.profile.api._

  class Profiles(tag: Tag) extends Table[UserProfile](tag, "profiles") {
    def id = column[String]("id", O.PrimaryKey)
    def firstName = column[String]("first_name")
    def lastName = column[String]("last_name")

    def * = (id, firstName, lastName) <> ((UserProfile.apply _).tupled, UserProfile.unapply)
  }

  protected val profiles = TableQuery[Profiles]

}
