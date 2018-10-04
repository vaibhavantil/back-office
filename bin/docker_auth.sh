#! /bin/bash
pip install --user awscli
export PATH=$PATH:$HOME/.local/bin
eval $(aws ecr get-login --no-include-email  --region $AWS_DEFAULT_REGION)
