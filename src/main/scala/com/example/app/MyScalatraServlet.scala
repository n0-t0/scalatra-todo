package com.example.app

import org.scalatra._
import scalikejdbc._
import scalikejdbc.config._

class MyScalatraServlet extends ScalatraServlet {
  
  // -------------------------------------
  // Class.forName("org.h2.Driver")
  // ConnectionPool.singleton("jdbc:mysql://localhost:3306/", "root", "my-secret-pw")

  // implicit val session = AutoSession

  // DB.autoCommit { implicit session => SQL("""
  //   use databasename
  // """).execute.apply()}
  // println("use databasename")

  // // drop extisting table
  // DB.autoCommit { implicit session => SQL("""
  //   drop table if exists members
  // """).execute.apply()
  // }

  // // execute DDL
  // DB.autoCommit { implicit session => SQL("""
  //   create table if not exists members (
  //     id bigint primary key auto_increment,
  //     name varchar(30) not null,
  //     description varchar(1000),
  //     birthday date,
  //     created_at timestamp not null
  //   )
  // """).execute.apply()}
  // println("create table members")


  // // insert initial data
  // DB localTx { implicit session =>
  //   val insertSql = SQL("insert into members (name, birthday, created_at) values (?, ?, ?)")
  //   insertSql.bind("Alice", Option(java.time.LocalDate.of(1980, 1, 1)), java.time.LocalDateTime.now()).update.apply()
  //   insertSql.bind("Bob", None, java.time.LocalDateTime.now()).update.apply()
  // }

  // // select
  // val members: List[Map[String, Any]] = DB readOnly { implicit session =>
  // SQL("select * from members").map(rs => rs.toMap).list.apply()}
  // println(members)
  // -------------------------------------

  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:mysql://localhost:3306/", "root", "my-secret-pw")

  DB.autoCommit { implicit session => SQL("""
    use databasename
  """).execute.apply()}
  println("use databasename")

  var taskSeq = Seq[String]()

  get("/") {
    
    // views.html.hello()
    if (params.get("task").isEmpty) {
      taskSeq = Seq.empty

      // drop extisting table
      DB.autoCommit { implicit session => SQL("""
        drop table if exists tasks
      """).execute.apply()
      }

      // execute DDL
      DB.autoCommit { implicit session => SQL("""
        create table if not exists tasks (
          id bigint primary key auto_increment,
          task varchar(30) not null,
          discription varchar(100),
          created_at timestamp not null
        )
      """).execute.apply()}
      println("create table tasks")

      views.html.index(taskSeq)
    }
    else {
      println(params("task") + " is added")
      // taskSeq = taskSeq :+ params("task")
      DB localTx { implicit session =>
        val task = params("task")
        val now = java.time.LocalDateTime.now()
        val insertSql = SQL("""
          insert into tasks (task, discription, created_at)
          values (?, ?, ?)
        """)
        insertSql.bind(task, None, now).update.apply()
      }

      val taskList: List[Map[String, Any]] = DB readOnly { implicit session =>
        SQL("select * from tasks").map(rs => rs.toMap).list.apply()
      }
      println(taskList)
      val taskSeq = taskList.map {map => map("task").toString()}
      println(taskSeq)
      views.html.index(taskSeq)
    }
  }

  post("/") {
  }

}
