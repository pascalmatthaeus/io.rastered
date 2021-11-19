#!/bin/bash
exec ./video-run.sh &
exec ./ome-run.sh &
exec ./frontend-run.sh &
exec ./proxy-run.sh &
exec ./api-run.sh;
