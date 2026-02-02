import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', name: 'Home', component: () => import('../views/HomeView.vue') },
  { path: '/employees', name: 'Employees', component: () => import('../views/EmployeesView.vue') },
  { path: '/employees/new', name: 'EmployeeNew', component: () => import('../views/EmployeeFormView.vue') },
  { path: '/employees/:id', name: 'EmployeeEdit', component: () => import('../views/EmployeeFormView.vue'), props: true },
  { path: '/shifts', name: 'Shifts', component: () => import('../views/ShiftsView.vue') },
  { path: '/vacation-requests', name: 'VacationRequests', component: () => import('../views/VacationRequestsView.vue') },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
