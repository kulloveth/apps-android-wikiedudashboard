package org.wikiedufoundation.wikiedudashboard.ui.profile.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.wikiedufoundation.wikiedudashboard.R
import org.wikiedufoundation.wikiedudashboard.data.preferences.SharedPrefs
import org.wikiedufoundation.wikiedudashboard.ui.adapters.ProfileCourseListRecyclerAdapter
import org.wikiedufoundation.wikiedudashboard.ui.coursedetail.common.view.CourseDetailActivity
import org.wikiedufoundation.wikiedudashboard.ui.profile.data.CourseData
import org.wikiedufoundation.wikiedudashboard.ui.profile.data.ProfileResponse
import org.wikiedufoundation.wikiedudashboard.util.showToast
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * Use the [ProfileCourseListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileCourseListFragment : Fragment() {

    private var mParam1: String? = null
    private var tv_no_courses: TextView? = null
    private var progressBar: ProgressBar? = null
    private var recyclerView: RecyclerView? = null
    private var coursesList: List<CourseData> = ArrayList()
    private var courseListRecyclerAdapter: ProfileCourseListRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            coursesList = (it.getSerializable(ARG_PARAM1) as ProfileResponse).courses ?: emptyList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_explore_course_list, container, false)
        progressBar = view.findViewById(R.id.progressBar)
        tv_no_courses = view.findViewById(R.id.tv_no_courses)
        recyclerView = view.findViewById(R.id.rv_course_list)

        val sharedPrefs: SharedPrefs? = context?.let { SharedPrefs(it) }
        tv_no_courses?.text = sharedPrefs?.cookies
        courseListRecyclerAdapter = ProfileCourseListRecyclerAdapter(R.layout.item_rv_explore_courses_users) {
            openCourseDetail(it)
        }
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView?.layoutManager = linearLayoutManager
        recyclerView?.setHasFixedSize(true)
        recyclerView?.adapter = courseListRecyclerAdapter
        setData(coursesList)
        showProgressBar(false)
        return view
    }

    fun setData(courses: List<CourseData>) {
        Timber.d(courses.toString())
        if (courses.isNotEmpty()) {
            recyclerView?.visibility = View.VISIBLE
            courseListRecyclerAdapter?.setData(courses)
            courseListRecyclerAdapter?.notifyDataSetChanged()
            tv_no_courses?.visibility = View.GONE
        } else {
            recyclerView?.visibility = View.GONE
            tv_no_courses?.visibility = View.VISIBLE
        }
    }

    fun showProgressBar(show: Boolean) {
        if (show) {
            progressBar?.visibility = View.VISIBLE
        } else {
            progressBar?.visibility = View.GONE
        }
    }

    fun showMessage(message: String) {
        context?.showToast(message)
    }

    private fun openCourseDetail(slug: String) {
        val i = Intent(context, CourseDetailActivity::class.java)
        i.putExtra("url", slug)
        i.putExtra("enrolled", false)
        startActivity(i)
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExploreFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: ProfileResponse): ProfileCourseListFragment {
            val fragment = ProfileCourseListFragment()
            val args = Bundle()
            args.putSerializable(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }
    }
}
