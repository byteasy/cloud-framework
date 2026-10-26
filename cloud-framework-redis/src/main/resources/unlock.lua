local key = KEYS[1];
local threadId = ARGV[1];

if(redis.call('exists', key) == 1) then
	if (redis.call('hget', key, 'threadId') == threadId) then
		redis.call('DEL', key);
	    return nil;
	end;
end;
