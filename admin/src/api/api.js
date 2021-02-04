import request from '@/utils/request'

export function getApiDetail(name) {
    return request({
        url: '/api/' + name,
        method: 'get',
    })
}

export function getApiList(params) {
    return request({
        url: '/api/list',
        method: 'get',
        params: params,
    })
}

export function registerApi(data) {
    return request({
        url: '/api',
        method: 'post',
        data
    })
}
