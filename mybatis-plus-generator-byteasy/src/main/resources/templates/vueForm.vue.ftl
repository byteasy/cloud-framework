<template>
    <easy-form>
        <template v-slot:title>${tableComment}</template>
        <template v-slot:form>
            <el-card class="w-full" shadow="always">
                <el-form :model="form" label-width="auto" size="small" ref="refForm" :rules="rules">
                    <#list table.fields as field>
                        <el-form-item label="${field.comment}" prop="${field.propertyName}">
                            <el-input v-model="form.${field.propertyName}" :readonly="!edit"
                                      <#if field.keyFlag>disabled</#if>/>
                        </el-form-item>
                    </#list>
                </el-form>
            </el-card>
        </template>
        <template v-slot:footer>
            <el-button size="small" type="primary" @click="onOkClick" icon="check">{{ I18n.t('ok') }}</el-button>
            <el-button size="small" @click="onCancelClick" icon="close">{{ I18n.t('cancel') }}</el-button>
        </template>
    </easy-form>
</template>

<script>
    import {onMounted, ref} from 'vue'
    import {useI18n} from 'vue-i18n'
    import EasyForm from '../../../../components/EasyForm.vue'

    import {saveOrUpdate,} from '../../../api/'

    export default {
        name: '${entity}Form',
        props: {
            edit: {
                type: Boolean,
                default: false
            },
            row: {
                type: Object,
                default: undefined
            }
        },
        components: {
            EasyForm,
        },
        setup(props, context) {
            const I18n = useI18n()
            const refForm = ref(null)
            const form = ref({})
            const rules = ref({
                <#list table.fields as field>
                ${field.propertyName}: [
                    <#if field.propertyType == "String">
                    {required: false, message: I18n.t('required'), trigger: 'blur'}, {
                        min: 4,
                        max: ${field.metaInfo.length},
                        message: '${field.comment}长度至少4位，最多${field.metaInfo.length}位',
                        trigger: 'blur'
                    }
                    <#else>
                    {required: false, message: I18n.t('required'), trigger: 'blur'}, {
                        min: 0,
                        max: 100000,
                        message: '${field.comment}的取值范文[0 ~ 100000]',
                        trigger: 'blur'
                    }
                    </#if>
                ],
                </#list>
            })

            onMounted(() => {
            })

            if (props.row !== undefined) {
                Object.assign(form.value, props.row)
            }

            function onOkClick() {
                if (props.edit === true) {
                    refForm.value.validate((valid) => {
                        if (valid) {
                            saveOrUpdate(form.value)
                                .then(() => {
                                    context.emit('ok', true)
                                })
                                .catch(() => {
                                    context.emit('ok', false)
                                })
                        }
                    })
                } else {
                    context.emit('ok', false)
                }
            }

            function onCancelClick() {
                context.emit('cancel', false)
            }

            return {
                I18n,
                form,
                refForm,
                rules,
                onOkClick,
                onCancelClick
            }
        }
    }
</script>

<style></style>
