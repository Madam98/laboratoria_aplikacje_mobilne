package com.example.jsonapp.DB

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.jsonapp.Model.ComModel
import com.example.jsonapp.Model.PostModel
import com.example.jsonapp.Model.TodosModel
import com.example.jsonapp.Model.UserModel

class DatabaseHandler(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "JSON"
        private val TABLE_USERS = "Users"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
        private val TABLE_TODOS = "Todos"
        private val KEY_USER_ID = "userId"
        private val KEY_TITLE = "title"
        private val KEY_COMPLETED = "completed"
        private val TABLE_POSTS = "Posts"
        private val KEY_BODY = "body"
        private val TABLE_COMS = "Comments"
        private val KEY_POST_ID = "postId"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_POSTS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_COMS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TODOS")
        val CREATE_USERS_TABLE = ("CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT" + ")")
        db?.execSQL(CREATE_USERS_TABLE)
        val CREATE_TODOS_TABLE = ("CREATE TABLE " + TABLE_TODOS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_ID + " INTEGER,"
                + KEY_TITLE + " TEXT," + KEY_COMPLETED + " TEXT," + "FOREIGN KEY("
                + KEY_USER_ID + ") REFERENCES " + TABLE_USERS +"(" + KEY_ID + "))")
        db?.execSQL(CREATE_TODOS_TABLE)
        val CREATE_POSTS_TABLE = ("CREATE TABLE " + TABLE_POSTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_ID + " INTEGER,"
                + KEY_TITLE + " TEXT," + KEY_BODY + " TEXT," + "FOREIGN KEY("
                + KEY_USER_ID + ") REFERENCES " + TABLE_USERS +"(" + KEY_ID + "))")
        db?.execSQL(CREATE_POSTS_TABLE)
        val CREATE_COMS_TABLE = ("CREATE TABLE " + TABLE_COMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_POST_ID + " INTEGER,"
                + KEY_NAME + " TEXT," + KEY_EMAIL + " TEXT," + KEY_BODY + " TEXT," + "FOREIGN KEY("
                + KEY_POST_ID + ") REFERENCES " + TABLE_POSTS +"(" + KEY_ID + "))")
        db?.execSQL(CREATE_COMS_TABLE)
    }

    fun addUser(emp: UserModel):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id)
        contentValues.put(KEY_NAME, emp.name)
        contentValues.put(KEY_EMAIL, emp.email )

        val success = db.insert(TABLE_USERS, null, contentValues)

        db.close()
        return success
    }

    fun addTodos(emp: TodosModel):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id)
        contentValues.put(KEY_USER_ID, emp.userid)
        contentValues.put(KEY_TITLE, emp.title)
        contentValues.put(KEY_COMPLETED, emp.completed )

        val success = db.insert(TABLE_TODOS, null, contentValues)

        db.close()
        return success
    }

    fun addPosts(emp: PostModel):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id)
        contentValues.put(KEY_USER_ID, emp.userid)
        contentValues.put(KEY_TITLE, emp.title)
        contentValues.put(KEY_BODY, emp.body )

        val success = db.insert(TABLE_POSTS, null, contentValues)

        db.close()
        return success
    }

    fun addComs(emp: ComModel):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.id)
        contentValues.put(KEY_POST_ID, emp.postid)
        contentValues.put(KEY_NAME, emp.name)
        contentValues.put(KEY_EMAIL, emp.email)
        contentValues.put(KEY_BODY, emp.body)

        val success = db.insert(TABLE_COMS, null, contentValues)

        db.close()
        return success
    }

    @SuppressLint("Range")
    fun viewUser():List<UserModel>{
        val empList:ArrayList<UserModel> = ArrayList<UserModel>()
        val selectQuery = "SELECT * FROM $TABLE_USERS"
        val db = this.readableDatabase
        var cursor: Cursor?
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var name: String
        var email: String
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                email = cursor.getString(cursor.getColumnIndex("email"))
                val emp= UserModel(id = id, name = name, email = email)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return empList
    }

    @SuppressLint("Range")
    fun countTodos(id: Int):Int{
        val selectQuery = "SELECT COUNT(*) FROM $TABLE_TODOS WHERE userId=$id"
        val db = this.readableDatabase
        var cursor: Cursor?
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return 0
        }
        var odp: Int = 0
        if (cursor.moveToFirst()) {
            odp = cursor.getInt(cursor.getColumnIndex("COUNT(*)"))
        }
        cursor.close()
        return odp
    }

    @SuppressLint("Range")
    fun countCompletedTodos(id: Int):Int{
        val selectQuery = "SELECT COUNT(*) FROM $TABLE_TODOS WHERE userId=$id AND completed=\"true\""
        val db = this.readableDatabase
        var cursor: Cursor?
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return 0
        }
        var odp: Int = 0
        if (cursor.moveToFirst()) {
            odp = cursor.getInt(cursor.getColumnIndex("COUNT(*)"))
        }
        cursor.close()
        return odp
    }

    @SuppressLint("Range")
    fun countPosts(id: Int):Int{
        val selectQuery = "SELECT COUNT(*) FROM $TABLE_POSTS WHERE userId=$id"
        val db = this.readableDatabase
        var cursor: Cursor?
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return 0
        }
        var odp: Int = 0
        if (cursor.moveToFirst()) {
            odp = cursor.getInt(cursor.getColumnIndex("COUNT(*)"))
        }
        cursor.close()
        return odp
    }

    @SuppressLint("Range")
    fun getIdFromName(name: String):Int{
        val selectQuery = "SELECT id FROM $TABLE_USERS WHERE name=\"$name\""
        val db = this.readableDatabase
        var cursor: Cursor?
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return 0
        }
        var odp: Int = 0
        if (cursor.moveToFirst()) {
            odp = cursor.getInt(cursor.getColumnIndex("id"))
        }
        cursor.close()
        return odp
    }

    @SuppressLint("Range")
    fun viewTodos(userid: Int):List<TodosModel>{
        val empList:ArrayList<TodosModel> = ArrayList<TodosModel>()
        val selectQuery = "SELECT * FROM $TABLE_TODOS WHERE userId=$userid"
        val db = this.readableDatabase
        var cursor: Cursor?
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var title: String
        var completed: String
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                title = cursor.getString(cursor.getColumnIndex("title"))
                if(cursor.getString(cursor.getColumnIndex("completed")) == "true"){
                    completed = "Tak"
                } else {
                    completed = "Nie"
                }
                val emp= TodosModel(id = id, userid = userid, title= title, completed=completed)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return empList
    }

    @SuppressLint("Range")
    fun viewPosts(userid: Int):List<PostModel>{
        val empList:ArrayList<PostModel> = ArrayList<PostModel>()
        val selectQuery = "SELECT * FROM $TABLE_POSTS WHERE userId=$userid"
        val db = this.readableDatabase
        var cursor: Cursor?
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var title: String
        var body: String
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                title = cursor.getString(cursor.getColumnIndex("title"))
                body = cursor.getString(cursor.getColumnIndex("body"))
                val emp= PostModel(id = id, userid = userid, title= title, body=body)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return empList
    }

    @SuppressLint("Range")
    fun getIdFromTitle(title: String):Int{
        val selectQuery = "SELECT id FROM $TABLE_POSTS WHERE title=\"$title\""
        val db = this.readableDatabase
        var cursor: Cursor?
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return 0
        }
        var odp: Int = 0
        if (cursor.moveToFirst()) {
            odp = cursor.getInt(cursor.getColumnIndex("id"))
        }
        cursor.close()
        return odp
    }

    @SuppressLint("Range")
    fun getBodyFromId(id: Int):String{
        val selectQuery = "SELECT body FROM $TABLE_POSTS WHERE id=$id"
        val db = this.readableDatabase
        var cursor: Cursor?
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return "Error"
        }
        var odp: String? = null
        if (cursor.moveToFirst()) {
            odp = cursor.getString(cursor.getColumnIndex("body"))
        }
        cursor.close()
        return odp.toString()
    }

    @SuppressLint("Range")
    fun viewComs(postid: Int):List<ComModel>{
        val empList:ArrayList<ComModel> = ArrayList<ComModel>()
        val selectQuery = "SELECT * FROM $TABLE_COMS WHERE postId=$postid"
        val db = this.readableDatabase
        var cursor: Cursor?
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var name: String
        var email: String
        var body: String
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                email = cursor.getString(cursor.getColumnIndex("email"))
                body = cursor.getString(cursor.getColumnIndex("body"))
                val emp= ComModel(id = id, postid = postid, name=name, email=email, body=body)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return empList
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Never implement, just leave it as it is")
    }
}