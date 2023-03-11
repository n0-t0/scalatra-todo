import com.example.app._
import org.scalatra._
import javax.servlet.ServletContext

import org.scalatra._
import scalikejdbc._
import scalikejdbc.config._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) = {
    context.mount(new MyScalatraServlet, "/*")
    myInit()
  }
  def myInit() = {
    Class.forName("com.mysql.cj.jdbc.Driver")
    ConnectionPool.singleton("jdbc:mysql://localhost:3306/", "root", "my-secret-pw")
    implicit val session = AutoSession

    DB.autoCommit {implicit session => SQL("""use databasename""").execute.apply()}
    println("use databasename")

    // // drop extisting table
    //   DB.autoCommit { implicit session => SQL("""
    //     drop table if exists tasks
    //   """).execute.apply()}

      DB.autoCommit { implicit session => SQL("""
        create table if not exists tasks (
          id bigint primary key auto_increment,
          user_id varchar(30) not null,
          task varchar(30) not null,
          description varchar(100),
          created_at timestamp not null
        )
      """).execute.apply()}
      DB.autoCommit { implicit session => SQL("""
        create table if not exists users (
          user_id varchar(30) not null primary key,
          password varchar(30) not null
        )
      """).execute.apply()}
      println("create table tasks, users")
      // DB.localTx { implicit session =>
    //   val insertSql = SQL("""
    //     insert into users (user_id, password)
    //     values (?, ?)
    //   """)
    //   insertSql.bind("mimimi", "aaa").update.apply()
    // }
  }
}
