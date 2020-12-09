# Dynamics
### Java로 만든 간단한 물리 역학 시뮬레이터입니다.
## 1. situation.json
#### 시뮬레이션의 초기상태를 지정하는 파일입니다.
> 1. `ParticleAutoGeneration`은 `true/false`를 가지며 `true`일시 입자를 지정한 갯수만큼 자동 생성합니다.
> 2. `NumberOfParticles`은 자동생성할 입자의 갯수를 지정합니다. `ParticleAutoGeneration`의 값이 `true`인 경우에만 적용됩니다.
> 3. `Particles`는 초기 입자들의 상태를 나타냅니다. `ParticleAutoGeneration`의 값이 `false`인 경우에만 적용됩니다. JSON Array꼴이며 각 원소는 다음과 같이 구성되어있는 JSON Object입니다.
> > 3.1. `Color`는 입자의 색을 지정합니다. RBG 16진수 값입니다.
> > 3.2. `LocationX`: 입자의 최초 x좌표 위치입니다.
> >
> > 3.3. `LocationY`: 입자의 최초 y좌표 위치입니다.
> >
> > 3.4. `HorizontalV`: 수평방향 속도입니다. 좌표계는 오른쪽이 +입니다.
> >
> > 3.5. `VerticalV`: 수직방향 속도입니다. 좌표계는 아래쪽이 +입니다.
> 4. `Gravity`는 중력을 사용할지 정합니다. `true/false`를 가지는 값이며 `true`일지 중력을 사용합니다.
> 5. `refresh`는 화면을 새로고침하는 주기입니다. 밀리초(1/1000초, ms)단위입니다.

## 2. 좌표계
> 1. 좌표계는 왼쪽이 -, 오른쪽이 +, 위쪽이 -, 아래쪽이 +입니다.
> 2. 벽에 충돌하는 기준은 다음과 같습니다.
> > 2.1. 입자의 y좌표가 0보다 작은 경우 위쪽 벽에 충돌했습니다.
> >
> > 2.2. 입자의 x좌표가 0보다 작은 경우 왼쪽 벽에 충돌했습니다.
> >
> > 2.3. 입자의 x좌표에 대해 495<=x인 경우 오른쪽 벽에 충돌했습니다.
> >
> > 2.4. 입자의 y좌표에 대해 495<=y인 경우 아래쪽 벽에 충돌했습니다.


## 3. 물리
> 1. 물체가 벽에 충돌한 경우 다음과 같이 계산됩니다.
> > 1.1. 물체가 위쪽, 아래쪽 벽에 충돌한 경우 수평방향 속도는 유지되며 수직방향 속도의 부호는 바뀝니다.
> >
> > 1.2. 물체가 왼쪽, 오른쪽 벽에 충돌한 경우 수직방향 속도는 유지되며 수평방향 속도의 부호는 바뀝니다.
> 2. 두 입자의 거리가 5.0 이하인 경우 두 입자는 충돌한것으로 판단됩니다. 이 경우 두 입자의 속도는 속도의 평균으로 결정됩니다.

## 4. 컨트롤
#### 모든 컨트롤은 `COMMAND HERE`에 다음 키를 입력함으로 진행됩니다.
> 1. `SPACE`로 시뮬레이션을 일시정지/진행할 수 있습니다.
> 2. `Enter`로 시뮬레이션을 진행하지 않고 그래픽만 랜더링할 수 있습니다.
> 3. `Shift`로 한 프레임만 시뮬레이션해서 랜더링할 수 있습니다.
> 4. `r`을 눌러 초기상태로 되돌릴 수 있습니다.
#### 이 외 UI는 다음과 같이 사용합니다.
#### 경고! UI는 입력값을 검사하지 않습니다. 잘못된 입력은 프로그램에 크래시를 일으킵니다.
> 1. `Add a particle`은 입자를 추가하는데 쓰이며 초기조건을 위치는 정수, 속도는 실수로 적고 추가할 수 있습니다.
> 2. `Trace a particle`은 입자의 ID를 이용해 한 개의 입자를 계속 추적할 수 있습니다. 위치, 속도가 표시되며 그래픽에서는 조금 더 크게 눈에 띄는 색으로 렌더링됩니다. 입자의 ID는 추가된 순서대로 0번부터 할당되며 수동으로 추가한 입자의 경우 JSON배열의 앞에 있는 입자부터 0번이 부여됩니다.