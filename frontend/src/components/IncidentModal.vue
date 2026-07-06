<template>
  <div class="modal fade" id="incidentModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">{{ isEdit ? 'Редактирование инцидента' : 'Новый инцидент' }}</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="submit">
            <div class="mb-3 w-40" style="float: left">
              <label class="form-label">Дата и время начала *</label>
              <input type="datetime-local" class="form-control" v-model="form.startTime" required />
            </div>

            <div class="mb-3 w-40" style="float: right">
              <label class="form-label">Дата и время окончания</label>
              <input type="datetime-local" class="form-control" v-model="form.endTime" />
            </div>
            <div class="mb-3">
              <label class="form-label">Описание инцидента</label>
              <textarea class="form-control" v-model="form.description" rows="3"></textarea>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-danger" @click="submit">Сохранить</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Выход</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { Modal } from 'bootstrap';
import api from '../api';

const modalInstance = ref(null);
const form = ref({ startTime: '', endTime: '', description: '' });
const loaderId = ref(null);
const incidentId = ref(null);
const isEdit = computed(() => incidentId.value !== null);

function open(ldrId, incident) {
  loaderId.value = ldrId;
  if (incident) {
    incidentId.value = incident.id;
    // Преобразуем даты в формат datetime-local
    form.value.startTime = incident.startTime ? incident.startTime.slice(0, 16) : '';
    form.value.endTime = incident.endTime ? incident.endTime.slice(0, 16) : '';
    form.value.description = incident.description;
  } else {
    incidentId.value = null;
    const now = new Date();
    const local = new Date(now.getTime() - now.getTimezoneOffset() * 60000);
    form.value.startTime = local.toISOString().slice(0, 16);
    form.value.endTime = '';
    form.value.description = '';
  }
  // Проверяем, что модальное окно инициализировано
  if (modalInstance.value) {
    modalInstance.value.show();
  } else {
    console.error('Модальное окно не инициализировано');
  }
}

function close() {
  if (modalInstance.value) {
    modalInstance.value.hide();
  }
}

async function submit() {
  try {
    const payload = {
      startTime: form.value.startTime,
      endTime: form.value.endTime || null,
      description: form.value.description
    };
    if (incidentId.value) {
      await api.put(`/loaders/${loaderId.value}/incidents/${incidentId.value}`, payload);
    } else {
      await api.post(`/loaders/${loaderId.value}/incidents`, payload);
    }
    emit('saved');
    close();
  } catch (error) {
    alert('Ошибка: ' + (error.response?.data || error.message));
  }
}

const emit = defineEmits(['saved']);

// Экспортируем метод open для родителя
defineExpose({ open });

onMounted(() => {
  const modalEl = document.getElementById('incidentModal');
  if (modalEl) {
    modalInstance.value = new Modal(modalEl);
  } else {
    console.error('Элемент модального окна не найден');
  }
});
</script>