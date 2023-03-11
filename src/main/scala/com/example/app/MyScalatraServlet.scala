package com.example.app

import org.scalatra._
import scalikejdbc._
import scalikejdbc.config._

    // parameter handling
    // validation and error handling
    // create model instance
    // uregister user database
    // register complete notification
    // redirect to main page

    // database connection
    // get user info
    // create user instance
  
class MyScalatraServlet extends ScalatraServlet {

  get("/") {

    val user_id = params("user_id")
    val dataList: List[Map[String, Any]] = DB readOnly { implicit session =>
      SQL("select * from tasks").map(rs => rs.toMap).list.apply()
    }
    val taskList = dataList.map {map => map("task").toString()}
    println(user_id + "'s tasks: " + taskList) // debug
    views.html.index(taskList, user_id)
  }

  post("/") {
    if (params("task") == "") {
      halt(400, "invalid task query parameter")
    }
    else {
      val user_id = params("user_id")
      val task = params("task")
      DB.localTx {  implicit session =>
        val insertSql = SQL("""
          insert into tasks (user_id, task, description, created_at)
          values (?, ?, ?, ?)
        """)
        println("new task "+ params("task") + " is added for " +params("user_id"))
        insertSql.bind(user_id, task, "", java.time.LocalDateTime.now()).update.apply()
      }
      redirect("/?user_id="+user_id)
    }
  }


  get("/login") {
    Class.forName("com.mysql.jdbc.Driver")
    ConnectionPool.singleton("jdbc:mysql://localhost:3306/", "root", "my-secret-pw")
    implicit val session = AutoSession

    DB.autoCommit {implicit session => SQL("""use databasename""").execute.apply()}
    println("use databasename")

    // drop extisting table
      DB.autoCommit { implicit session => SQL("""
        drop table if exists tasks
      """).execute.apply()}

      // execute DDL
      DB.autoCommit { implicit session => SQL("""
        create table if not exists tasks (
          id bigint primary key auto_increment,
          user_id varchar(30) not null,
          task varchar(30) not null,
          description varchar(100),
          created_at timestamp not null
        )
      """).execute.apply()}
      println("create table tasks")

      DB.localTx {  implicit session =>
        val insertSql = SQL("""
            insert into tasks (user_id, task, description, created_at)
            values (?, ?, ?, ?)
          """)
        insertSql.bind("Jon", "task1", "aaa", java.time.LocalDateTime.now()).update.apply()
      }
      DB.localTx {  implicit session =>
        val insertSql = SQL("""
            insert into tasks (user_id, task, description, created_at)
            values (?, ?, ?, ?)
          """)
        insertSql.bind("Jon", "task2", "aaa", java.time.LocalDateTime.now()).update.apply()
      }
      views.html.login()

  }
}


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
