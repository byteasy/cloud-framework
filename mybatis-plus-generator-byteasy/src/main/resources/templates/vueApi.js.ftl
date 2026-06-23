import request from '../../request'
import glb from '../../global'

export function getPage(params) {
return request({
url: '/' + glb.APP_NAME + '/pri/${entityName}s',
method: 'get',
params: params
})
}

export function getById(id) {
return request({
url: '/' + glb.APP_NAME + '/pri/${entityName}s/' + id,
method: 'get'
})
}

export function saveOrUpdate(params) {
return request({
url: '/' + glb.APP_NAME + '/pri/${entityName}s',
method: 'post',
data: params
})
}

export function batchSave(params) {
return request({
url: '/' + glb.APP_NAME + '/pri/${entityName}s/batch',
method: 'post',
data: params
})
}

export function updateById(params) {
return request({
url: '/' + glb.APP_NAME + '/pri/${entityName}s',
method: 'put',
data: params
})
}

export function batchUpdate(params) {
return request({
url: '/' + glb.APP_NAME + '/pri/${entityName}s/batch',
method: 'put',
data: params
})
}

export function removeById(id) {
return request({
url: '/' + glb.APP_NAME + '/pri/${entityName}s/' + id,
method: 'delete'
})
}

export function removeByIds(entities) {
return request({
url: '/' + glb.APP_NAME + '/pri/${entityName}s/batch',
method: 'delete',
data: entities
})
}

export function removeByWrapper(entity) {
return request({
url: '/' + glb.APP_NAME + '/pri/${entityName}s',
method: 'delete',
data: entity
})
}

export function downloadToFile(params) {
return request({
url: '/' + glb.APP_NAME + '/pri/${entityName}s/batch/file',
method: 'get',
params: params
})
}


export function downloadTemplate(params) {
return request({
url: '/' + glb.APP_NAME + '/pri/${entityName}s/batch/file/template',
method: 'get',
params: params
})
}

export function uploadByFile(entity) {
return request({
url: '/' + glb.APP_NAME + '/pri/${entityName}s/batch/file',
method: 'post',
data: entity
})
}
