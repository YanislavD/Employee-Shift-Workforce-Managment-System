<template>
  <div>
    <h1>Заявки за отпуск</h1>

    <!-- Форма за нова заявка -->
    <div class="card">
      <h2>Нова заявка за отпуск</h2>
      <form @submit.prevent="submitRequest">
        <label>Служител</label>
        <select v-model="form.employeeId" required>
          <option value="">— избери —</option>
          <option v-for="e in (employees || []).filter(x => x.active)" :key="e.id" :value="e.id">
            {{ e.firstName }} {{ e.lastName }}
          </option>
        </select>
        <label>От дата</label>
        <input v-model="form.startDate" type="date" required />
        <label>До дата</label>
        <input v-model="form.endDate" type="date" required />
        <label>Причина (по желание)</label>
        <textarea v-model="form.reason" rows="2" placeholder="Напр. планов годишен отпуск"></textarea>
        <p v-if="formError" class="error">{{ formError }}</p>
        <button type="submit" class="btn btn-primary" :disabled="submitting">Изпрати заявка</button>
      </form>
    </div>

    <!-- Списък с заявки -->
    <div class="card">
      <h2>Всички заявки</h2>
      <p v-if="loading">Зареждане...</p>
      <p v-else-if="error" class="error">{{ error }}</p>
      <table v-else-if="list.length">
        <thead>
          <tr>
            <th>Служител</th>
            <th>От</th>
            <th>До</th>
            <th>Статус</th>
            <th>Причина</th>
            <th v-if="canApprove">Действия</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="v in list" :key="v.id">
            <tr>
              <td>{{ v.employeeName }}</td>
              <td>{{ v.startDate }}</td>
              <td>{{ v.endDate }}</td>
              <td>
                <span :class="['badge', 'badge-' + v.status?.toLowerCase()]">{{ statusText(v.status) }}</span>
                <span v-if="v.rejectionReason" class="rejection-reason" :title="v.rejectionReason">
                  {{ v.rejectionReason }}
                </span>
              </td>
              <td>{{ v.reason || '–' }}</td>
              <td v-if="canApprove">
                <template v-if="v.status === 'PENDING'">
                  <button type="button" class="btn btn-approve" @click="approve(v.id)">Одобри</button>
                  <button type="button" class="btn btn-danger" @click="startReject(v.id)">Отхвърли</button>
                </template>
                <template v-else>—</template>
              </td>
            </tr>
            <tr v-if="rejecting === v.id" class="reject-row">
              <td :colspan="canApprove ? 6 : 5">
                <label>Причина за отхвърляне:</label>
                <input v-model="rejectReason" type="text" placeholder="Въведи причина..." @keyup.enter="confirmReject" />
                <button type="button" class="btn btn-primary" @click="confirmReject">Потвърди</button>
                <button type="button" class="btn btn-secondary" @click="cancelReject">Отказ</button>
              </td>
            </tr>
          </template>
        </tbody>
      </table>
      <p v-else>Няма заявки за отпуск.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, inject, onMounted } from 'vue'
import { vacationRequests as api } from '../api/client'

const currentUser = inject('currentUser')
const employees = inject('employees')

const list = ref([])
const loading = ref(true)
const error = ref('')
const submitting = ref(false)
const formError = ref('')

const form = ref({
  employeeId: '',
  startDate: '',
  endDate: '',
  reason: ''
})

const rejecting = ref(null)
const rejectReason = ref('')

const canApprove = computed(() => {
  const u = currentUser?.value
  return u && (u.role === 'MANAGER' || u.role === 'ADMIN')
})

function statusText(status) {
  const map = { PENDING: 'Чакаща', APPROVED: 'Одобрена', REJECTED: 'Отхвърлена' }
  return map[status] || status
}

async function load() {
  loading.value = true
  error.value = ''
  try {
    const res = await api.list()
    list.value = res.data
  } catch (e) {
    error.value = e.response?.data?.error || e.message || 'Грешка при зареждане'
  } finally {
    loading.value = false
  }
}

async function submitRequest() {
  formError.value = ''
  if (!form.value.employeeId || !form.value.startDate || !form.value.endDate) {
    formError.value = 'Попълни всички задължителни полета.'
    return
  }
  if (form.value.endDate < form.value.startDate) {
    formError.value = 'Крайната дата трябва да е след началната.'
    return
  }
  submitting.value = true
  try {
    await api.create({
      employeeId: Number(form.value.employeeId),
      startDate: form.value.startDate,
      endDate: form.value.endDate,
      reason: form.value.reason || null
    })
    form.value = { employeeId: '', startDate: '', endDate: '', reason: '' }
    await load()
  } catch (e) {
    formError.value = e.response?.data?.error || e.message || 'Грешка при изпращане'
  } finally {
    submitting.value = false
  }
}

function startReject(id) {
  rejecting.value = id
  rejectReason.value = ''
}

function cancelReject() {
  rejecting.value = null
  rejectReason.value = ''
}

async function confirmReject() {
  if (!rejecting.value || !currentUser?.value) return
  try {
    await api.updateStatus(rejecting.value, {
      status: 'REJECTED',
      approvedById: currentUser.value.id,
      rejectionReason: rejectReason.value || null
    })
    cancelReject()
    await load()
  } catch (e) {
    error.value = e.response?.data?.error || e.message || 'Грешка при отхвърляне'
  }
}

async function approve(id) {
  if (!currentUser?.value) return
  try {
    await api.updateStatus(id, {
      status: 'APPROVED',
      approvedById: currentUser.value.id
    })
    await load()
  } catch (e) {
    error.value = e.response?.data?.error || e.message || 'Грешка при одобряване'
  }
}

onMounted(() => {
  load()
  if (currentUser?.value && !form.value.employeeId) {
    form.value.employeeId = String(currentUser.value.id)
  }
})
</script>

<style scoped>
.badge { padding: 0.2rem 0.5rem; border-radius: 4px; font-size: 0.85rem; }
.badge-pending { background: #fff3e0; color: #e65100; }
.badge-approved { background: #e8f5e9; color: #2e7d32; }
.badge-rejected { background: #ffebee; color: #c62828; }
.rejection-reason { display: block; font-size: 0.8rem; color: #666; margin-top: 0.2rem; max-width: 200px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.reject-row { background: #fff8e1; padding: 1rem; }
.reject-row input { max-width: 300px; margin-right: 0.5rem; }
.reject-row .btn { margin-right: 0.5rem; }
.btn-approve { background: #2e7d32; color: white; }
.btn-approve:hover { background: #1b5e20; }
h2 { margin: 0 0 1rem 0; font-size: 1.1rem; }
</style>
