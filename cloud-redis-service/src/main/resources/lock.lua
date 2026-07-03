local key = KEYS[1]; 
local threadId = ARGV[1];
local releaseTime = ARGV[2];
local level = ARGV[3];

if(redis.call('exists', key) == 0) then
    redis.call('hset', key, 'threadId', threadId);
    redis.call('hset', key, 'level', level);
    redis.call('expire', key, releaseTime);
    return 1;
end;

local level_old = redis.call('hget', key, 'level');
if(level > level_old) then
 	redis.call('hset', key, 'threadId', threadId);
    redis.call('hset', key, 'level', level);
    redis.call('expire', key, releaseTime);
    return 1;
end;

local threadId_old = redis.call('hget', key, 'threadId');
if(threadId == threadId_old) then
 	redis.call('hset', key, 'threadId', threadId);
    redis.call('hset', key, 'level', level);
    redis.call('expire', key, releaseTime);
    return 1;
end;

return 0;