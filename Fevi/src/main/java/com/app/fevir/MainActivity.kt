package com.app.fevir


import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import com.app.fevir.adapter.MenuListAdapter
import com.app.fevir.adapter.dto.MenuList
import com.app.fevir.movie.list.MovieListFragment
import java.util.*


class MainActivity : AppCompatActivity() {

    private var mPlanetTitles: Array<String>? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var mDrawerList: ListView? = null
    private var mTitle: CharSequence? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private var leftDrawer: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mTitle = resources.getString(R.string.app_name)

        val menus = resources.getStringArray(R.array.menu_array)
        mPlanetTitles = menus
        mDrawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        mDrawerList = findViewById(R.id.left_drawer_list) as ListView
        leftDrawer = findViewById(R.id.left_drawer) as LinearLayout


        val menuLists = ArrayList<MenuList>()
        menuLists.add(MenuList(resources.getDrawable(R.drawable.appbar_star_invincible_color), menus[0]))
        menuLists.add(MenuList(resources.getDrawable(R.drawable.appbar_flag_bear_color), menus[1]))
        menuLists.add(MenuList(resources.getDrawable(R.drawable.appbar_hardware_headset_color), menus[2]))
        menuLists.add(MenuList(resources.getDrawable(R.drawable.appbar_bike_color), menus[3]))
        menuLists.add(MenuList(resources.getDrawable(R.drawable.appbar_controller_snes_color), menus[4]))

        val menuListAdapter = MenuListAdapter(this, R.layout.drawer_list_item, menuLists)
        // Set the adapter for the list view
        mDrawerList!!.adapter = menuListAdapter
        // Set the list's click listener
        mDrawerList!!.onItemClickListener = DrawerItemClickListener()
        val toolbar = setUpActionbar()

        mDrawerToggle = object : ActionBarDrawerToggle(
                this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                toolbar,
                R.string.drawer_open, /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */) {

            /** Called when a drawer has settled in a completely closed state.  */
            override fun onDrawerClosed(view: View?) {
                supportActionBar!!.title = mTitle
                invalidateOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state.  */
            override fun onDrawerOpened(drawerView: View?) {
                supportActionBar!!.title = mTitle
                invalidateOptionsMenu()
            }
        }

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout!!.setDrawerListener(mDrawerToggle)


        if (savedInstanceState == null) {
            selectItem(0)
        }

    }

    private fun setUpActionbar(): Toolbar {

        val toolbar = findViewById(R.id.toolbar_main) as Toolbar
        setSupportActionBar(toolbar)

        return toolbar
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle!!.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle!!.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle!!.onOptionsItemSelected(item)) {
            return true
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item)
    }

    /**
     * Swaps fragments in the main content view
     */
    private fun selectItem(position: Int) {

        val fragment = MovieListFragment.newInstance(position)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content_frame, fragment)
        fragmentTransaction.commit()


        // Highlight the selected item, update the title, and close the drawer
        mDrawerList!!.setItemChecked(position, true)
        setTitle(mPlanetTitles!![position])
        mDrawerLayout!!.closeDrawer(leftDrawer)
    }

    override fun setTitle(title: CharSequence) {
        mTitle = title
        supportActionBar!!.title = mTitle
    }

    private inner class DrawerItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            selectItem(position)
        }
    }


}
