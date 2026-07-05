<template>
  <div>
    <!-- Панель поиска и кнопки -->
    <div class="row mb-3">
      <div class="col-md-12">
        <div class="input-group mb-2">
          <input type="text" class="form-control" v-model="searchNumber" placeholder="Номер погрузчика" />
          <button class="btn btn-danger me-2" @click="search"><i class="bi bi-search"></i>&nbsp;Искать</button>
          <span></span><button class="btn btn-link" @click="resetFilter"><i class="bi bi-x-lg" style="font-size: 1rem; color: red;"></i>Сбросить фильтр</button>
        </div>
        <div>
          <button class="btn btn-danger me-2" @click="addLoader">Добавить</button>
        </div>
      </div>
    </div>

    <div style="max-height: 600px; overflow-y: auto; overflow-x: hidden">
      <!-- Таблица погрузчиков -->
      <table class="table table-striped table-hover align-middle">
        <thead>
          <tr>
            <th>Код</th>
            <th>Марка</th>
            <th>Номер</th>
            <th>Грузоподъ-<br/>мность</th>
            <th>Активен</th>
            <th>Изменен</th>
            <th>Пользователь</th>
            <th style="width: 110px !important;">Действия</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="loader in loaders" :key="loader.id"
              :class="{ 'table-primary': selectedLoader && selectedLoader.id === loader.id }"
              @click="selectLoader(loader)">
            <td v-if="editingLoader && editingLoader.id === loader.id">{{ editingLoader.id }}</td>
            <td v-else>{{ loader.id }}</td>

            <td>
              <input v-if="editingLoader && editingLoader.id === loader.id" v-model="editingLoader.brand" class="form-control form-control-sm" />
              <span v-else>{{ loader.brand }}</span>
            </td>
            <td>
              <input v-if="editingLoader && editingLoader.id === loader.id" v-model="editingLoader.number" class="form-control form-control-sm" />
              <span v-else>{{ loader.number }}</span>
            </td>
            <td>
              <input v-if="editingLoader && editingLoader.id === loader.id" v-model.number="editingLoader.capacity" type="number" step="0.001" class="form-control form-control-sm" />
              <span v-else>{{ loader.capacity }}</span>
            </td>
            <td>
              <input v-if="editingLoader && editingLoader.id === loader.id" type="checkbox" v-model="editingLoader.active" />
              <input v-else type="checkbox" v-model="loader.active" disabled/>
  <!--            <span v-else>{{ loader.active ? 'Да' : 'Нет' }}</span>-->
            </td>
            <td>{{ formatDateTime(loader.lastModified) }}</td>
            <td>{{ loader.modifiedBy }}</td>
            <td>
              <button v-if="selectedLoader && selectedLoader.id === loader.id && !editingLoader" class="btn" :disabled="!(selectedLoader && selectedLoader.id === loader.id)" @click="editLoader"><i class="bi bi-pencil-fill" style="font-size: 1.2rem; color: cornflowerblue;"></i></button>
              <button v-if="editingLoader && editingLoader.id === loader.id" class="btn" :disabled="!(editingLoader && editingLoader.id === loader.id)" @click="saveLoader"><i class="bi bi-check-lg" style="font-size: 1.2rem; color: cornflowerblue;"></i></button>
              <button v-if="selectedLoader && selectedLoader.id === loader.id && !editingLoader"  :disabled="!(selectedLoader && selectedLoader.id === loader.id)" class="btn" @click="deleteLoader"><i class="bi bi-x-lg" style="font-size: 1.2rem; color: cornflowerblue;"></i></button>
              <button v-if="editingLoader && editingLoader.id === loader.id"  :disabled="!(editingLoader && editingLoader.id === loader.id)" class="btn" @click="cancelEdit"><i class="bi bi-x-lg" style="font-size: 1.2rem; color: cornflowerblue;"></i></button>
            </td>
          </tr>
          <!-- Строка добавления -->
          <tr v-if="isNewRow">
            <td>—</td>
            <td><input v-model="newLoader.brand" class="form-control form-control-sm" placeholder="Марка" /></td>
            <td><input v-model="newLoader.number" class="form-control form-control-sm" placeholder="Номер" /></td>
            <td><input v-model.number="newLoader.capacity" type="number" step="0.001" class="form-control form-control-sm" placeholder="Грузоподъемность" /></td>
            <td><input type="checkbox" v-model="newLoader.active" /></td>
            <td colspan="2"></td>
            <td>
              <button class="btn" @click="saveNewLoader"><i class="bi bi-check-lg" style="font-size: 1.3rem; color: cornflowerblue;"></i></button>
              <button class="btn" @click="cancelNewLoader"><i class="bi bi-x-lg" style="font-size: 1.3rem; color: cornflowerblue;"></i></button>
  <!--            <button class="btn btn-primary btn-sm me-1" @click="saveNewLoader">Сохранить</button>-->
  <!--            <button class="btn btn-secondary btn-sm" @click="cancelNewLoader">Отменить</button>-->
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import api from '../api';

const props = defineProps({
  incidentCount: {
    type: Number,
    default: 0
  }
});

const emit = defineEmits(['select-loader']);

const loaders = ref([]);
const selectedLoader = ref(null);
const searchNumber = ref('');

// Редактирование
const editingLoader = ref(null);
const isEditing = computed(() => !!editingLoader.value);

// Новая запись
const newLoader = ref({ brand: '', number: '', capacity: 0, active: true });
const isNewRow = ref(false);

async function fetchLoaders() {
  try {
    const response = await api.get('/loaders', { params: { number: searchNumber.value || undefined } });
    loaders.value = response.data;
    // Если выбранный погрузчик ещё есть в списке, обновляем ссылку на объект
    if (selectedLoader.value) {
      const found = loaders.value.find(l => l.id === selectedLoader.value.id);
      if (found) {
        selectedLoader.value = found;
      } else {
        selectedLoader.value = null;
        emit('select-loader', null);
      }
    }
  } catch (error) {
    console.error('Ошибка загрузки:', error);
  }
}

function search() { fetchLoaders(); }
function resetFilter() { searchNumber.value = ''; fetchLoaders(); }

function selectLoader(loader) {
  if (isNewRow.value) return;
  if (isEditing.value) return; // не даём менять выбор во время редактирования
  selectedLoader.value = loader;
  emit('select-loader', loader.id);
}

// Добавление
function addLoader() {
  if (isNewRow.value) return;
  editingLoader.value = null;
  newLoader.value = { brand: '', number: '', capacity: 0, active: true };
  isNewRow.value = true;
  selectedLoader.value = null;
  emit('select-loader', null); // очищаем выбор в родителе
}

async function saveNewLoader() {
  if (!newLoader.value.brand || !newLoader.value.number) {
    alert('Марка и номер обязательны');
    return;
  }
  try {
    await api.post('/loaders', newLoader.value);
    isNewRow.value = false;
    await fetchLoaders();
    // если нужно выбрать последний добавленный – можно, но не обязательно
  } catch (error) {
    alert('Ошибка сохранения: ' + (error.response?.data || error.message));
  }
}

function cancelNewLoader() {
  isNewRow.value = false;
  newLoader.value = { brand: '', number: '', capacity: 0, active: true };
}

// Редактирование
function editLoader() {
  if (!selectedLoader.value || isNewRow.value) return;
  if (isEditing.value) return;
  editingLoader.value = { ...selectedLoader.value };
}

async function saveLoader() {
  if (!editingLoader.value) return;
  try {
    await api.put(`/loaders/${editingLoader.value.id}`, editingLoader.value);
    editingLoader.value = null;
    await fetchLoaders();
    // повторно эмитим выбранный, чтобы обновить простои (если нужно)
    if (selectedLoader.value) {
      emit('select-loader', selectedLoader.value.id);
    }
  } catch (error) {
    alert('Ошибка сохранения: ' + (error.response?.data || error.message));
  }
}

function cancelEdit() {
  if (!confirm('Не сохранять внесенные изменения? Вы уверены?')) return;
  editingLoader.value = null;
}

async function deleteLoader() {
  if (!selectedLoader.value || isNewRow.value) return;
  if (props.incidentCount > 0) {
    alert('Удаление запрещено: для погрузчика зарегистрированы простои');
    return;
  }
  if (!confirm('Удалить погрузчик? Вы уверены?')) return;
  try {
    await api.delete(`/loaders/${selectedLoader.value.id}`);
    selectedLoader.value = null;
    emit('select-loader', null); // очищаем выбор
    await fetchLoaders();
  } catch (error) {
    alert('Ошибка удаления: ' + (error.response?.data || error.message));
  }
}

function formatDateTime(dateStr) {
  if (!dateStr) return '';
  const dt = new Date(dateStr);
  return dt.toLocaleString('ru-RU');
}

onMounted(fetchLoaders);
</script>