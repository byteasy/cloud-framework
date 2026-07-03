<template>
    <easy-container>
        <template v-slot:queryBar>
            <el-input v-model="queryParam.name" style="width: 240px" placeholder="名称" size="small" clearable
                      @keyup.enter="refresh"/>
            <el-button type="primary" size="small" @click="refresh" icon="Search">{{ $t('search') }}</el-button>
            <el-button type="success" size="small" @click="addClick" icon="Plus" v-permission="'${entity}SaveOrUpdate'">
                {{ $t('add') }}
            </el-button>
        </template>
        <template v-slot:easyTable>
            <el-table
                    style="position: absolute;"
                    height="100%"
                    v-loading="loading"
                    :data="tableData.records"
                    size="small"
                    stripe
                    highlight-current-row
                    :empty-text="$t('table.emptyText')"
                    :element-loading-text="$t('table.loading')">
                <#list table.fields as field>
                    <el-table-column prop="${field.propertyName}" label="${field.comment}"/>
                </#list>
                <el-table-column :label="$t('table.operation')" width="220">
                    <template #default="scope">
                        <el-link type="primary" @click="viewClick(scope.row)" :underline="false">
                            <span class="text-xs p-1">{{ $t('table.view') }}</span>
                        </el-link>
                        <el-link type="primary" @click="editClick(scope.row)" :underline="false"
                                 v-permission="'${entity}SaveOrUpdate'">
                            <span class="text-xs p-1">{{ $t('table.edit') }}</span>
                        </el-link>
                        <el-popconfirm :confirm-button-text="$t('ok')" :cancel-button-text="$t('cancel')"
                                       :title="$t('deleteWarn')" @confirm="deleteClick(scope.row)">
                            <template #reference>
                                <el-link type="danger" :underline="false" v-permission="'${entity}RemoveById'">
                                    <span class="text-xs p-1">{{ $t('table.delete') }}</span>
                                </el-link>
                            </template>
                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>
        </template>
        <template v-slot:easyPagination>
            <el-pagination size="small" background layout="total, sizes, prev, pager, next, jumper"
                           :total="parseInt(tableData.total)" :default-page-size="20" @change="onPageChange"/>
        </template>
    </easy-container>
</template>
<script>
    import {openModal} from 'jenesius-vue-modal'
    import {onMounted, ref} from 'vue'
    import {useI18n} from 'vue-i18n'
    import EntityForm from './EntityForm.vue'
    import EasyContainer from '../../../components/EasyContainer.vue'
    import {ElMessage} from 'element-plus'
    import {getPage, removeById} from '../../../api/'

    export default {
        name: '${entity}List',
        components: {EasyContainer},
        emits: ['change'],
        setup() {
            const {t} = useI18n()
            const queryParam = ref({current: 0, size: 20})
            const tableData = ref({records: []})
            const loading = ref(true)

            onMounted(() => {
                refresh()
            })

            function refresh() {
                loading.value = true
                getPage(queryParam.value).then((rsp) => {
                    tableData.value = rsp.data
                    loading.value = false
                })
            }

            function onPageChange(currentPage, pageSize) {
                queryParam.value.current = currentPage
                queryParam.value.size = pageSize
                refresh()
            }

            async function addClick() {
                const modal = await openModal(EntityForm, {
                    edit: true,
                    row: {enabled: true, attributes: {locale: ['zh-CN']}}
                }, {backgroundClose: false})
                modal.on('ok', (value) => {
                    if (value) {
                        ElMessage({
                            message: t('operationSuccess'),
                            type: 'success'
                        })
                        refresh()
                    }
                    modal.close()
                })
                modal.on('cancel', () => {
                    modal.close()
                })
            }

            async function editClick(row) {
                const modal = await openModal(EntityForm, {edit: true, row: row}, {backgroundClose: false})
                modal.on('ok', (value) => {
                    if (value) {
                        ElMessage({
                            message: t('operationSuccess'),
                            type: 'success'
                        })
                        refresh()
                    }
                    modal.close()
                })
                modal.on('cancel', () => {
                    modal.close()
                })
            }

            async function viewClick(row) {
                const modal = await openModal(EntityForm, {edit: false, row: row}, {backgroundClose: false})
                modal.on('ok', (value) => {
                    if (value) {
                        refresh()
                    }
                    modal.close()
                })
                modal.on('cancel', () => {
                    modal.close()
                })
            }

            async function deleteClick(row) {
                removeById(row.id).then(() => {
                    ElMessage({
                        message: t('operationSuccess'),
                        type: 'success'
                    })
                    refresh()
                })
            }

            return {
                queryParam,
                tableData,
                loading,
                refresh,
                onPageChange,
                addClick,
                editClick,
                viewClick,
                deleteClick
            }
        }
    }
</script>
