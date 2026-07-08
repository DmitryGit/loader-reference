<template>
  <div class="mt-4">
    <h4><strong>Простои по погрузчику {{loaderNumber}}</strong></h4>
    <div class="mb-2">
      <button class="btn btn-danger me-2" @click="openAddModal">Добавить</button>
    </div>
    <div v-if="loaderId" style="overflow-y: scroll; overflow-x: auto; height: clamp(150px, 60vh, calc(80vh - 50px));">
      <table class="table table-sm">
        <thead>
          <tr>
            <th>Начало</th>
            <th>Окончание</th>
            <th>Причина</th>
            <th>Время простоя</th>
            <th width="100">Действия</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="inc in incidents" :key="inc.id"
              :class="{ 'table-info': selectedIncident && selectedIncident.id === inc.id }"
              @click="selectIncident(inc)">
            <td>{{ formatDateTime(inc.startTime) }}</td>
            <td>{{ inc.endTime ? formatDateTime(inc.endTime) : '—' }}</td>
            <td>{{ inc.description }}</td>
            <td>{{ inc.downtime }}</td>
            <td>
              <button v-if="selectedIncident && selectedIncident.id === inc.id" class="btn" @click="openEditModal"><i class="bi bi-pencil-fill" style="font-size: 1.2rem; color: cornflowerblue;"></i></button>
              <button v-if="selectedIncident && selectedIncident.id === inc.id"  class="btn" @click="deleteIncident"><i class="bi bi-x-lg" style="font-size: 1.2rem; color: cornflowerblue;"></i></button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-else class="text-muted">Выберите погрузчик для просмотра простоев</div>

    <incident-modal ref="incidentModal" @saved="handleIncidentSaved" />
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import api from '../api';
import IncidentModal from './IncidentModal.vue';

const props = defineProps({
  loaderId: Number,
  loaderNumber: String
});

const emit = defineEmits(['incident-count']);

const incidents = ref([]);
const selectedIncident = ref(null);
const incidentModal = ref(null);

async function fetchIncidents() {
  if (!props.loaderId) {
    // Если нет погрузчика, передаём 0
    emit('incident-count', 0);
    return;
  }
  try {
    const res = await api.get(`/loaders/${props.loaderId}/incidents`);
    incidents.value = res.data;
    emit('incident-count', incidents.value.length);
  } catch (error) {
    console.error('Ошибка загрузки простоев:', error);
    emit('incident-count', 0);
  }
}

function selectIncident(inc) {
  selectedIncident.value = inc;
}

function openAddModal() {
  if (incidentModal.value) {
    incidentModal.value.open(props.loaderId, null);
  } else {
    console.error('Модальное окно не доступно');
  }
}

function openEditModal() {
  if (!selectedIncident.value) return;
  if (incidentModal.value) {
    incidentModal.value.open(props.loaderId, selectedIncident.value);
  }
}

async function deleteIncident() {
  if (!selectedIncident.value) return;
  if (!confirm('Удалить информацию о простое? Вы уверены?')) return;
  try {
    await api.delete(`/loaders/${props.loaderId}/incidents/${selectedIncident.value.id}`);
    selectedIncident.value = null;
    await fetchIncidents(); // обновит список и эмитнет новое количество
  } catch (error) {
    alert('Ошибка удаления: ' + (error.response?.data || error.message));
  }
}

function handleIncidentSaved() {
  fetchIncidents(); // обновит список и эмитнет новое количество
}

function formatDateTime(dateStr) {
  if (!dateStr) return '';
  const dt = new Date(dateStr);
  return dt.toLocaleString('ru-RU');
}

watch(() => props.loaderId, () => {
  selectedIncident.value = null;
  fetchIncidents();
}, { immediate: true });
</script>